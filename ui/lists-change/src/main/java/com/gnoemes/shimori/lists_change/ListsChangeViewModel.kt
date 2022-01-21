package com.gnoemes.shimori.lists_change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.model.rate.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListsChangeViewModel @Inject constructor(
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