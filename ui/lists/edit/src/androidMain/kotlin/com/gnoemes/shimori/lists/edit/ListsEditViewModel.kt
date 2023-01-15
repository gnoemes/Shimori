package com.gnoemes.shimori.lists.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.utils.Logger
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal class ListsEditViewModel(
    savedStateHandle: SavedStateHandle,
    observeTitle: ObserveTitleWithTrackEntity,
    observePinExist: ObservePinExist,
    private val listsStateBus: ListsStateBus,
    private val toggleListPin: ToggleTitlePin,
    private val createOrUpdateTrack: CreateOrUpdateTrack,
    private val deleteTrack: DeleteTrack,
    private val logger: Logger,
) : ViewModel() {
    private val targetId: Long = savedStateHandle["id"]!!
    private val targetType: TrackTargetType = savedStateHandle["type"]!!
    private val markComplete: Boolean = savedStateHandle["markComplete"] ?: false
    private val deleteNotification: Boolean = savedStateHandle["deleteNotification"] ?: false

    private val _uiEvents = MutableSharedFlow<UiEvents>()
    private val _state = MutableStateFlow(ListsEditViewState.Empty)

    private var track: Track? = null

    val uiEvents: SharedFlow<UiEvents> get() = _uiEvents
    val state: StateFlow<ListsEditViewState> get() = _state

    init {
        viewModelScope.launch {
            combine(
                observeTitle.flow,
                observePinExist.flow
            ) { entity, pinned ->
                track = entity?.track
                val title = entity?.entity

                ListsEditViewState(
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
            }.collect { _state.value = it }
        }

        observeTitle(ObserveTitleWithTrackEntity.Params(targetId, targetType))
        observePinExist(ObservePinExist.Params(targetId, targetType))
    }

    fun onStatusChanged(newStatus: TrackStatus) {
        viewModelScope.launch {
            _state.value = _state.value.copy(status = newStatus)
        }
    }

    fun onProgressChanged(newValue: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(progress = newValue)
        }
    }

    fun onRewatchesChanged(newValue: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(rewatches = newValue)
        }
    }

    fun onScoreChanged(newValue: Int?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(score = newValue)
        }
    }

    fun onCommentChanged(newComment: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(comment = newComment)
        }
    }

    fun onProgressInput() {
        viewModelScope.launch {
            _state.value = _state.value.copy(inputState = ListEditInputState.Progress)
        }
    }

    fun onRewatchingInput() {
        viewModelScope.launch {
            _state.value = _state.value.copy(inputState = ListEditInputState.Rewatching)
        }
    }

    fun onCommentInput() {
        viewModelScope.launch {
            _state.value = _state.value.copy(inputState = ListEditInputState.Comment)
        }
    }

    fun onNoneInput() {
        viewModelScope.launch {
            _state.value = _state.value.copy(inputState = ListEditInputState.None)
        }
    }

    fun togglePin() {
        viewModelScope.launch {
            _state.value = _state.value.copy(pinned = !_state.value.pinned)
        }
    }

    fun delete() {
        viewModelScope.launch {
            val track = track
            val image = _state.value.title?.image
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
        viewModelScope.launch {
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