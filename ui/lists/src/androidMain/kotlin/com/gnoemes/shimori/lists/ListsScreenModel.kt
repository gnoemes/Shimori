package com.gnoemes.shimori.lists

import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.api.UiMessageManager
import com.gnoemes.shimori.common.ui.navigation.StateScreenModel
import com.gnoemes.shimori.common.ui.utils.MessageID
import com.gnoemes.shimori.common.ui.utils.ShimoriTextProvider
import com.gnoemes.shimori.common.ui.utils.get
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.data.list.ListsUiEvents
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.interactors.ToggleTitlePin
import com.gnoemes.shimori.domain.interactors.UpdateTitleTracks
import com.gnoemes.shimori.domain.observers.ObservePinsExist
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.domain.observers.ObserveTracksExist
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ListsScreenModel(
    private val stateBus: ListsStateBus,
    private val updateTitleTracks: UpdateTitleTracks,
    private val textProvider: ShimoriTextProvider,
    private val togglePin: ToggleTitlePin,
    private val updateTrack: CreateOrUpdateTrack,
    observeTracksExist: ObserveTracksExist,
    observePinsExist: ObservePinsExist,
    observeShikimoriAuth: ObserveShikimoriAuth,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<ListScreenState>(ListScreenState.Loading, dispatchers) {

    private val uiMessageManager = UiMessageManager()

    init {
        ioCoroutineScope.launch {
            combine(
                stateBus.type.observe,
                stateBus.page.observe,
                observePinsExist.flow,
                observeTracksExist.flow,
                stateBus.tracksLoading.observe,
                uiMessageManager.message,
            ) { type, status, hasPins, hasTracks, isLoading, message ->
                if (isLoading) ListScreenState.Loading
                else if (type == ListType.Pinned && !hasPins) ListScreenState.NoPins
                else if (!hasTracks) ListScreenState.NoTracks(type)
                else ListScreenState.Data(
                    type = type,
                    status = status,
                    uiMessage = message
                )
            }.collectLatest { state ->
                mutableState.update { state }
            }
        }

        ioCoroutineScope.launch {
            combine(
                stateBus.type.observe,
                stateBus.page.observe,
                observeShikimoriAuth.flow,
                stateBus.tracksLoading.observe,
            ) { type, page, auth, loading ->
                val trackType = type.trackType ?: return@combine null

                //prevent double sync
                if (loading) return@combine null

                Triple(trackType, page, auth.isAuthorized)
            }
                .filterNotNull()
                .distinctUntilChanged()
                .filter { it.third }
                .map { it.first to it.second }
                .collect(::updatePage)
        }

        coroutineScope.launch {
            stateBus.uiEvents.observe
                .collect(::showUiEvent)
        }

        observeShikimoriAuth(Unit)
        observePinsExist(Unit)
        observeTracksExist(Unit)
    }

    fun onMessageShown(id: Long) {
        coroutineScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun onMessageAction(id: Long) {
        coroutineScope.launch {
            val payload = uiMessageManager.message.firstOrNull()?.payload

            when (id) {
                MESSAGE_TOGGLE_PIN -> (payload as? TitleWithTrackEntity)?.let(::togglePin)
                MESSAGE_INCREMENTER_UPDATE -> (payload as? Track)?.let(::undoIncrementerProgress)
                MESSAGE_TRACK_DELETED -> (payload as? Track)?.let(::createTrack)
            }
        }
    }

    private fun createTrack(track: Track) {
        coroutineScope.launch {
            updateTrack(CreateOrUpdateTrack.Params(track)).collect()
        }
    }

    private fun showUiEvent(event: ListsUiEvents) {
        when (event) {
            is ListsUiEvents.PinStatusChanged -> showPinStatusChanged(event.title, event.pinned)
            is ListsUiEvents.IncrementerProgress -> showIncrementerProgress(
                event.title,
                event.oldTrack,
                event.newProgress
            )

            is ListsUiEvents.TrackDeleted -> showTrackDeleted(
                event.image,
                event.track
            )
        }
    }

    private fun showTrackDeleted(image: ShimoriImage?, track: Track) {
        coroutineScope.launch {
            uiMessageManager.emitMessage(
                UiMessage(
                    id = MESSAGE_TRACK_DELETED,
                    message = textProvider[MessageID.TrackDeleted],
                    action = textProvider[MessageID.Undo],
                    image = image,
                    payload = track
                )
            )
        }
    }

    private fun showIncrementerProgress(
        title: TitleWithTrackEntity,
        oldTrack: Track,
        newProgress: Int
    ) {
        coroutineScope.launch {
            uiMessageManager.emitMessage(
                UiMessage(
                    id = MESSAGE_INCREMENTER_UPDATE,
                    message = textProvider[MessageID.IncrementerFormat].format(
                        oldTrack.progress,
                        newProgress
                    ),
                    image = title.entity.image,
                    action = textProvider[MessageID.Undo],
                    payload = oldTrack
                )
            )
        }
    }

    private fun showPinStatusChanged(
        title: TitleWithTrackEntity,
        pinned: Boolean
    ) {
        coroutineScope.launch {
            val message =
                if (pinned) MessageID.TitlePinned
                else MessageID.TitleUnPinned

            uiMessageManager.emitMessage(
                UiMessage(
                    id = MESSAGE_TOGGLE_PIN,
                    message = textProvider[message],
                    action = textProvider[MessageID.Undo],
                    image = title.entity.image,
                    payload = title
                )
            )
        }
    }

    private fun togglePin(entity: TitleWithTrackEntity) {
        coroutineScope.launch {
            togglePin(ToggleTitlePin.Params(entity.type, entity.id)).collect()
        }
    }

    private fun undoIncrementerProgress(track: Track) {
        coroutineScope.launch {
            updateTrack.invoke(CreateOrUpdateTrack.Params(track)).collect()
        }
    }

    private fun updatePage(pair: Pair<TrackTargetType, TrackStatus>) {
        val (type, status) = pair
        coroutineScope.launch {
            updateTitleTracks(
                UpdateTitleTracks.Params.optionalUpdate(
                    type = type,
                    status = status
                )
            ).collect()
        }
    }

    companion object {
        private const val MESSAGE_TOGGLE_PIN = 1L
        private const val MESSAGE_INCREMENTER_UPDATE = 2L
        private const val MESSAGE_TRACK_DELETED = 3L
    }
}

internal sealed class ListScreenState() {
    object Loading : ListScreenState()
    object NoPins : ListScreenState()
    data class NoTracks(val type: ListType) : ListScreenState()
    data class Data(
        val type: ListType = ListType.Anime,
        val status: TrackStatus = TrackStatus.WATCHING,
        val uiMessage: UiMessage? = null
    ) : ListScreenState()
}

internal const val INCREMENTATOR_MAX_PROGRESS = 50