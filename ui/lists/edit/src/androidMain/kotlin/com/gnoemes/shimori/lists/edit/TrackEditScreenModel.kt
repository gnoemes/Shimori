package com.gnoemes.shimori.lists.edit

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.StateScreenModel
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.data.list.ListsUiEvents
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.interactors.DeleteTrack
import com.gnoemes.shimori.domain.interactors.ToggleTitlePin
import com.gnoemes.shimori.domain.observers.ObservePinExist
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal class TrackEditScreenModel(
    navData: FeatureScreen.TrackEdit,
    observeTitle: ObserveTitleWithTrackEntity,
    observePinExist: ObservePinExist,
    private val listsStateBus: ListsStateBus,
    private val toggleListPin: ToggleTitlePin,
    private val createOrUpdateTrack: CreateOrUpdateTrack,
    private val deleteTrack: DeleteTrack,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<TrackEditScreenState>(TrackEditScreenState(), dispatchers) {
    private val targetId: Long = navData.id
    private val targetType: TrackTargetType = navData.type
    private val markComplete: Boolean = navData.markComplete
    private val deleteNotification: Boolean = navData.deleteNotification

    private val _uiEvents = MutableSharedFlow<UiEvents>()

    private var track: Track? = null

    val uiEvents: SharedFlow<UiEvents> get() = _uiEvents

    init {
        ioCoroutineScope.launch {
            combine(
                observeTitle.flow,
                observePinExist.flow
            ) { entity, pinned ->
                track = entity?.track
                val title = entity?.entity

                TrackEditScreenState(
                    title = title,
                    status = (if (markComplete) TrackStatus.COMPLETED else track?.status)
                        ?: TrackStatus.WATCHING,
                    progress = (if (markComplete) title?.size else track?.progress) ?: 0,
                    rewatches = track?.reCounter ?: 0,
                    score = track?.score,
                    comment = track?.comment,
                    type = targetType,
                    pinned = pinned,
                    newTrack = track == null
                )
            }.collectLatest { state ->
                mutableState.update { state }
            }
        }

        observeTitle(ObserveTitleWithTrackEntity.Params(targetId, targetType))
        observePinExist(ObservePinExist.Params(targetId, targetType))
    }

    fun onStatusChanged(newStatus: TrackStatus) {
        coroutineScope.launch {
            mutableState.update { it.copy(status = newStatus) }
        }
    }

    fun onProgressChanged(newValue: Int) {
        coroutineScope.launch {
            mutableState.update { it.copy(progress = newValue) }
        }
    }

    fun onRewatchesChanged(newValue: Int) {
        coroutineScope.launch {
            mutableState.update { it.copy(rewatches = newValue) }
        }
    }

    fun onScoreChanged(newValue: Int?) {
        coroutineScope.launch {
            mutableState.update { it.copy(score = newValue) }
        }
    }

    fun onCommentChanged(newComment: String?) {
        coroutineScope.launch {
            mutableState.update { it.copy(comment = newComment) }
        }
    }

    fun onProgressInput() {
        coroutineScope.launch {
            mutableState.update { it.copy(inputState = TrackEditInputState.Progress) }
        }
    }

    fun onRewatchingInput() {
        coroutineScope.launch {
            mutableState.update { it.copy(inputState = TrackEditInputState.Rewatching) }
        }
    }

    fun onCommentInput() {
        coroutineScope.launch {
            mutableState.update { it.copy(inputState = TrackEditInputState.Comment) }
        }
    }

    fun onNoneInput() {
        coroutineScope.launch {
            mutableState.update { it.copy(inputState = TrackEditInputState.None) }
        }
    }

    fun togglePin() {
        coroutineScope.launch {
            mutableState.update { it.copy(pinned = !it.pinned) }
        }
    }

    fun delete() {
        coroutineScope.launch {
            val track = track
            val image = state.value.title?.image
            track?.id?.let {
                deleteTrack(DeleteTrack.Params(it)).collect { status ->
                    if (status.isSuccess) {
                        if (deleteNotification) {
                            listsStateBus.uiEvents(
                                ListsUiEvents.TrackDeleted(
                                    image,
                                    track
                                )
                            )
                        }

                        _uiEvents.emit(UiEvents.NavigateUp)
                    }
                }
            }
        }
    }

    fun createOrUpdate() {
        coroutineScope.launch {
            val state = state.value
            val track = Track(
                id = track?.id ?: 0,
                targetId = targetId,
                targetType = targetType,
                status = state.status,
                score = state.score,
                comment = state.comment,
                progress = state.progress,
                reCounter = state.rewatches,
                dateCreated = track?.dateCreated,
                dateUpdated = Clock.System.now()
            )

            toggleListPin(
                ToggleTitlePin.Params(
                    type = targetType,
                    id = targetId,
                    pin = state.pinned
                )
            ).collect()

            createOrUpdateTrack(params = CreateOrUpdateTrack.Params(track))
                .collect {
                    if (it.isSuccess) {
                        _uiEvents.emit(UiEvents.NavigateUp)
                    }
                }
        }
    }
}

@Immutable
internal data class TrackEditScreenState(
    val title: ShimoriTitleEntity? = null,
    val status: TrackStatus = TrackStatus.PLANNED,
    val progress: Int = 0,
    val rewatches: Int = 0,
    val score: Int? = null,
    val comment: String? = null,
    val type: TrackTargetType = TrackTargetType.ANIME,
    val inputState: TrackEditInputState = TrackEditInputState.None,
    val pinned: Boolean = false,
    val newTrack: Boolean = false,
)

internal sealed class UiEvents {
    object NavigateUp : UiEvents()
}

internal sealed class TrackEditInputState {
    object None : TrackEditInputState()
    object Progress : TrackEditInputState()
    object Rewatching : TrackEditInputState()
    object Comment : TrackEditInputState()
}
