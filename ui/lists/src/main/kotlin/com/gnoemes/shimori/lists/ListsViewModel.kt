package com.gnoemes.shimori.lists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shikimori.ShikimoriAuthManager
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeShikimoriAuth: ObserveShikimoriAuth,
    observeRateSort: ObserveRateSort,
    private val updateRateSort: UpdateRateSort,
    shikimoriAuthManager: ShikimoriAuthManager
) : ViewModel(), ShikimoriAuthManager by shikimoriAuthManager {
    private val listType: RateTargetType = RateTargetType.findOrDefault(null)

    private val pendingActions = MutableSharedFlow<ListsAction>()

    val state = MutableStateFlow(ListsViewState.Empty)

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is ListsAction.UpdateListSort -> updateRateSort(action.option, action.isDescending)
                }

            }
        }

        viewModelScope.launch {
            combine(
                    observeShikimoriAuth.observe().distinctUntilChanged(),
                    observeRateSort.observe().distinctUntilChanged()
            ) { auth, activeRateSort ->
                ListsViewState(
                        authStatus = auth,
                        type = listType,
                        activeSort = activeRateSort ?: RateSort.defaultForType(listType),
                )
            }.collect { state.emit(it) }
        }

        observeShikimoriAuth(Unit)
        observeRateSort(ObserveRateSort.Params(listType))
    }

    private fun updateRateSort(option: RateSortOption, isDescending: Boolean) {
        viewModelScope.launch {
            updateRateSort(UpdateRateSort.Params(type = listType, sort = option, isDescending = isDescending)).collect()
        }
    }

    fun submitAction(action: ListsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}