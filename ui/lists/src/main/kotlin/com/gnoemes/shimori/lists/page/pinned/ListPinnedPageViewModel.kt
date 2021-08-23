package com.gnoemes.shimori.lists.page.pinned

import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObservePinnedTitles
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.lists.page.BaseListPageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListPinnedPageViewModel @Inject constructor(
    observeRateSort: ObserveRateSort,
    stateManager: ListsStateManager,
    getRandomTitleWithStatus: GetRandomTitleWithStatus,
    togglePin: ToggleListPin,
    observePinnedTitles: ObservePinnedTitles,
) : BaseListPageViewModel(null, observeRateSort, stateManager, getRandomTitleWithStatus, togglePin) {
    private val _state = MutableStateFlow(ListPinnedViewState.Empty)

    val state: StateFlow<ListPinnedViewState> get() = _state

    init {
        viewModelScope.launch {
            observePinnedTitles.flow.collect {
                val state = ListPinnedViewState(it)
                _state.emit(state)
            }
        }

        observePinnedTitles(Unit)
    }
}