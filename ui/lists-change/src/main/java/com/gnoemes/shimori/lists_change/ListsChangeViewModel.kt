package com.gnoemes.shimori.lists_change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListsChangeViewModel @Inject constructor(
    private val listsStateManager: ListsStateManager,
    private val prefs: ShimoriPreferences
) : ViewModel() {

    val currentType get() = listsStateManager.type.value
    val currentPage get() = listsStateManager.page.value

    fun openRandomTitle() {
        viewModelScope.launch {
            listsStateManager.openRandomTitleEvent(Unit)
        }
    }

    fun onPageOpened(newType: ListType, status: RateStatus) {
        if (newType != currentType) changeListType(newType)
        viewModelScope.launch {
            listsStateManager.page(status)
            prefs.preferredListStatus = status.shikimoriValue
        }
    }

    fun changeListType(newType: ListType) {
        viewModelScope.launch {
            listsStateManager.type(newType)
            prefs.preferredListType = newType.type
        }
    }
}