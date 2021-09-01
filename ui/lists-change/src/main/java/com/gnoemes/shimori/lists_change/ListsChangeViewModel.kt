package com.gnoemes.shimori.lists_change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.model.rate.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListsChangeViewModel @Inject constructor(
    private val listsStateManager: ListsStateManager,
    private val prefs: ShimoriPreferences
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ListsChangeAction>()

    val currentType get() = listsStateManager.type.value

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is ListsChangeAction.Random -> openRandomTitle()
                    is ListsChangeAction.ChangeListType -> changeListType(action.listType)
                }
            }
        }
    }

    fun submitAction(action: ListsChangeAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }


    private fun openRandomTitle() {
        viewModelScope.launch {
            listsStateManager.openRandomTitleEvent(Unit)
        }
    }

    private fun changeListType(newType: ListType) {
        viewModelScope.launch {
            listsStateManager.type(newType)
            prefs.preferredListType = newType.type
        }
    }
}