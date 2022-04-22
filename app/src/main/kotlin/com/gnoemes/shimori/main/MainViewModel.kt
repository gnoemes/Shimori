package com.gnoemes.shimori.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.common.ui.utils.ObservableLoadingCounter
import com.gnoemes.shimori.data.list.ListsStateManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel constructor(
    private val listsStateManager: ListsStateManager,
) : ViewModel() {

    private val updatingUserDataState = ObservableLoadingCounter()

    val state: StateFlow<MainViewState> = listsStateManager.type.observe
        .map(::MainViewState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = MainViewState.Empty
        )

    init {
        viewModelScope.launch {
            updatingUserDataState
                .observable
                .collect { listsStateManager.ratesLoading.update(it) }
        }
    }
}