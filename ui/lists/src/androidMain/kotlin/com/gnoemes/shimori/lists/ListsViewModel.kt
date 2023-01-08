package com.gnoemes.shimori.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.api.UiMessageManager
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
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObservePinsExist
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.domain.observers.ObserveTracksExist
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListsViewModel(
    private val stateBus: ListsStateBus,
    private val updateTitleTracks: UpdateTitleTracks,
    private val textProvider: ShimoriTextProvider,
    private val togglePin: ToggleTitlePin,
    private val updateTrack: CreateOrUpdateTrack,
    observeTracksExist: ObserveTracksExist,
    observePinsExist: ObservePinsExist,
    observeMyUser: ObserveMyUserShort,
    observeShikimoriAuth: ObserveShikimoriAuth,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()

    val state = combine(
        stateBus.type.observe,
        stateBus.page.observe,
        observeMyUser.flow,
        observePinsExist.flow,
        observeTracksExist.flow,
        stateBus.tracksLoading.observe,
        uiMessageManager.message,
    ) { type, status, user, hasPins, hasTracks, isLoading, message ->
        ListsViewState(
            type = type,
            status = status,
            user = user,
            isEmpty = if (type == ListType.Pinned) !hasPins else !hasTracks,
            hasTracks = hasTracks,
            isLoading = isLoading,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ListsViewState.Empty
    )

    init {
        viewModelScope.launch {
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

        viewModelScope.launch {
            stateBus.uiEvents.observe
                .collect(::showUiEvent)
        }

        observeShikimoriAuth(Unit)
        observeMyUser(Unit)
        observePinsExist(Unit)
        observeTracksExist(Unit)
    }

    fun onMessageShown(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun onMessageAction(id: Long) {
        viewModelScope.launch {
            val payload = uiMessageManager.message.firstOrNull()?.payload

            when (id) {
                MESSAGE_TOGGLE_PIN -> (payload as? TitleWithTrackEntity)?.let(::togglePin)
                MESSAGE_INCREMENTER_UPDATE -> (payload as? Track)?.let(::undoIncrementerProgress)
                MESSAGE_TRACK_DELETED -> (payload as? Track)?.let(::createTrack)
            }
        }
    }

    private fun createTrack(track: Track) {
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch {
            togglePin(ToggleTitlePin.Params(entity.type, entity.id)).collect()
        }
    }

    private fun undoIncrementerProgress(track: Track) {
        viewModelScope.launch {
            updateTrack.invoke(CreateOrUpdateTrack.Params(track)).collect()
        }
    }

    private fun updatePage(pair: Pair<TrackTargetType, TrackStatus>) {
        val (type, status) = pair
        viewModelScope.launch {
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