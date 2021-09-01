package com.gnoemes.shimori.lists_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateRate
import com.gnoemes.shimori.domain.interactors.DeleteRate
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveEntityWithRate
import com.gnoemes.shimori.domain.observers.ObserveHasPin
import com.gnoemes.shimori.model.ShikimoriContentEntity
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListsEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeEntityWithRate: ObserveEntityWithRate,
    observeHasPin: ObserveHasPin,
    private val createOrUpdateRate: CreateOrUpdateRate,
    private val deleteRate: DeleteRate,
    private val toggleListPin: ToggleListPin,
) : ViewModel() {
    private val targetId: Long = savedStateHandle.get("id")!!
    private val targetType: RateTargetType = savedStateHandle.get("type")!!

    private val pendingActions = MutableSharedFlow<ListsEditAction>()

    private val _state = MutableStateFlow(ListsEditViewState.Empty)

    private var rateId: Long? = null

    val state: StateFlow<ListsEditViewState> get() = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is ListsEditAction.StatusChanged -> onStatusChanged(action.newStatus)
                    is ListsEditAction.ProgressChanged -> onProgressChanged(action.newValue)
                    is ListsEditAction.RewatchesChanged -> onRewatchesChanged(action.newValue)
                    is ListsEditAction.ScoreChanged -> onScoreChanged(action.newValue)
                    is ListsEditAction.CommentChanged -> onCommentChanged(action.newComment)
                    ListsEditAction.TogglePin -> togglePin()
                    ListsEditAction.Delete -> delete()
                    ListsEditAction.Save -> createOrUpdate()
                }
            }
        }

        viewModelScope.launch {
            combine(
                    observeEntityWithRate.flow,
                    observeHasPin.flow
            ) { entity, pinned ->
                val rate = entity?.rate
                val shikimoriEntity = entity?.entity as? ShikimoriContentEntity

                rateId = rate?.id

                ListsEditViewState(
                        image = shikimoriEntity?.image,
                        //TODO romadzi
                        name = shikimoriEntity?.name.orEmpty(),
                        status = rate?.status ?: RateStatus.PLANNED,
                        progress = rate?.progress ?: 0,
                        size = shikimoriEntity?.size,
                        rewatches = rate?.reCounter ?: 0,
                        score = rate?.score,
                        comment = rate?.comment,
                        type = targetType,
                        pinned = pinned
                )
            }.collect { _state.value = it }


        }

        observeEntityWithRate(ObserveEntityWithRate.Params(targetId, targetType))
        observeHasPin(ObserveHasPin.Params(targetId, targetType))
    }

    fun submitAction(action: ListsEditAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    private fun onStatusChanged(newStatus: RateStatus) {
        viewModelScope.launch {
            _state.value = _state.value.copy(status = newStatus)
        }
    }

    private fun onProgressChanged(newValue: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(progress = newValue)
        }
    }

    private fun onRewatchesChanged(newValue: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(rewatches = newValue)
        }
    }

    private fun onScoreChanged(newValue: Int?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(score = newValue)
        }
    }

    private fun onCommentChanged(newComment: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(comment = newComment)
        }
    }

    private fun togglePin() {
        viewModelScope.launch {
            _state.value = _state.value.copy(pinned = !_state.value.pinned)
        }
    }

    private fun delete() {
        viewModelScope.launch {
            rateId?.let { deleteRate(DeleteRate.Params(it)) }
        }
    }

    private fun createOrUpdate() {
        viewModelScope.launch {

        }
    }

}