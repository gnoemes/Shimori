package com.gnoemes.shimori.lists.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.domain.observers.ObserveListsPages
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListTabsViewModel @Inject constructor(
    observeListsPages: ObserveListsPages,
    private val stateManager: ListsStateManager
) : ViewModel() {
    private val _state = MutableStateFlow(ListTabsViewState.Empty)

    val state: StateFlow<ListTabsViewState> get() = _state

    init {
        viewModelScope.launch {
            combine(
                    stateManager.type.observe.mapNotNull { it.rateType }.distinctUntilChanged(),
                    observeListsPages.flow
            ) { type, pages ->
                ListTabsViewState(
                        type = type,
                        pages = pages
                )
            }.collect {
                _state.value = it
            }
        }


        viewModelScope.launch {
            stateManager.type.observe
                .mapNotNull { it.rateType }
                .distinctUntilChanged()
                .collect { type ->
                    observeListsPages(ObserveListsPages.Params(type = type))
                }
        }
    }

    fun onPageChanged(newPage: RateStatus) {
        viewModelScope.launch {
            stateManager.page(newPage)
        }
    }
}