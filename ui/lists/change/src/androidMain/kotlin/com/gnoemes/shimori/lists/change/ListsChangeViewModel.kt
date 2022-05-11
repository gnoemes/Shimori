package com.gnoemes.shimori.lists.change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.list.ListsStateManager
import kotlinx.coroutines.launch

internal class ListsChangeViewModel(
    private val listsStateManager: ListsStateManager,
) : ViewModel() {
    fun openRandomTitle() {
        viewModelScope.launch {
            listsStateManager.openRandomTitleEvent(Unit)
        }
    }

    fun openPinned() {
        viewModelScope.launch {
            listsStateManager.type(ListType.Pinned)
        }
    }
}