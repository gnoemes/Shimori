package com.gnoemes.shimori.lists.change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.list.ListsStateBus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ListsChangeViewModel(
    private val listsStateBus: ListsStateBus,
) : ViewModel() {

    val type = listsStateBus.type
        .observe
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ListType.Anime
        )

    fun openRandomTitle() {
        viewModelScope.launch {
            listsStateBus.openRandomTitleEvent(Unit)
        }
    }

    fun openPinned() {
        viewModelScope.launch {
            listsStateBus.type(ListType.Pinned)
        }
    }
}