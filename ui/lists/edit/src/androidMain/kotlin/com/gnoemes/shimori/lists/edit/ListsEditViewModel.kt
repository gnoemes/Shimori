package com.gnoemes.shimori.lists.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.entities.InvokeSuccess
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateRate
import com.gnoemes.shimori.domain.interactors.DeleteRate
import com.gnoemes.shimori.domain.interactors.ToggleTitlePin
import com.gnoemes.shimori.domain.observers.ObservePinExist
import com.gnoemes.shimori.domain.observers.ObserveTitleWithRateEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal class ListsEditViewModel(
    savedStateHandle: SavedStateHandle,
    observeTitle: ObserveTitleWithRateEntity,
    observePinExist: ObservePinExist,
    private val toggleListPin: ToggleTitlePin,
    private val createOrUpdateRate: CreateOrUpdateRate,
    private val deleteRate: DeleteRate,
    private val logger: Logger,
) : ViewModel() {
    private val targetId: Long = savedStateHandle["id"]!!
    private val targetType: RateTargetType = savedStateHandle["type"]!!

    private val _uiEvents = MutableSharedFlow<UiEvents>()
    private val _state = MutableStateFlow(ListsEditViewState.Empty)

    private var rate: Rate? = null
    private var targetShikimoriId: Long? = null

    val uiEvents: SharedFlow<UiEvents> get() = _uiEvents
    val state: StateFlow<ListsEditViewState> get() = _state

    init {
        viewModelScope.launch {
            combine(
                observeTitle.flow,
                observePinExist.flow
            ) { entity, pinned ->
                rate = entity?.rate
                val shikimoriEntity = entity?.entity

                targetShikimoriId = shikimoriEntity?.shikimoriId

                ListsEditViewState(
                    image = shikimoriEntity?.image,
                    //TODO romadzi
                    name = shikimoriEntity?.name.orEmpty(),
                    status = rate?.status ?: RateStatus.WATCHING,
                    progress = rate?.progress ?: 0,
                    size = shikimoriEntity?.size,
                    rewatches = rate?.reCounter ?: 0,
                    score = rate?.score,
                    comment = rate?.comment,
                    type = targetType,
                    pinned = pinned,
                    newRate = rate == null
                )
            }.collect { _state.value = it }
        }

        observeTitle(ObserveTitleWithRateEntity.Params(targetId, targetType))
        observePinExist(ObservePinExist.Params(targetId, targetType))
    }

    fun onStatusChanged(newStatus: RateStatus) {
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
            rate?.id?.let {
                deleteRate(DeleteRate.Params(it)).collect {
                    _uiEvents.emit(UiEvents.NavigateUp)
                }
            }
        }
    }

    fun createOrUpdate() {
        viewModelScope.launch {
            val state = state.value
            val rate = Rate(
                id = rate?.id ?: 0,
                shikimoriId = rate?.shikimoriId ?: 0,
                targetId = targetShikimoriId ?: 0,
                targetType = targetType,
                status = state.status,
                score = state.score,
                comment = state.comment,
                progress = state.progress,
                reCounter = state.rewatches,
                dateCreated = rate?.dateCreated,
                dateUpdated = Clock.System.now()
            )

            toggleListPin(
                ToggleTitlePin.Params(
                    type = targetType,
                    id = targetId,
                    pin = state.pinned
                )
            ).collect()

            createOrUpdateRate(params = CreateOrUpdateRate.Params(rate))
                .collect {
                    if (it is InvokeSuccess) {
                        _uiEvents.emit(UiEvents.NavigateUp)
                    }
                }
        }
    }

}