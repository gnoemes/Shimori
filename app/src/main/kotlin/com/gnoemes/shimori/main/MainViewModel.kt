package com.gnoemes.shimori.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.entities.InvokeSuccess
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.common.utils.ObservableLoadingCounter
import com.gnoemes.shimori.common.utils.collectInto
import com.gnoemes.shimori.domain.interactors.UpdateRates
import com.gnoemes.shimori.domain.interactors.UpdateUser
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.lists.ListsStateManager
import com.gnoemes.shimori.model.rate.ListType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefs: ShimoriPreferences,
    observeShikimoriAuth: ObserveShikimoriAuth,
    private val updateUser: UpdateUser,
    private val updateRates: UpdateRates,
    private val listsStateManager: ListsStateManager,
) : ViewModel() {

    private val updatingUserDataState = ObservableLoadingCounter()
    private val _state = MutableStateFlow(MainViewState.Empty)

    private val pendingActions = MutableSharedFlow<MainAction>()

    val state: StateFlow<MainViewState> get() = _state

    init {

        viewModelScope.launch {
            combine(
                    listsStateManager.currentType,
                    observeShikimoriAuth.flow
            ) { listType, authState ->
                MainViewState(
                        listType = listType,
                        authState = authState
                )
            }.collect { _state.emit(it) }
        }

        viewModelScope.launch {
            _state
                .map { it.authState }
                .distinctUntilChanged()
                .collect {
                    if (it.isAuthorized) updateUserAndRates()
                }
        }

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    MainAction.Random -> openRandomTitle()
                    is MainAction.ChangeListType -> changeListType(action.listType)
                }
            }
        }

        viewModelScope.launch {
            updatingUserDataState.observable.collect { listsStateManager.updatingRates(it) }
        }

        observeShikimoriAuth(Unit)
    }

    internal fun submitAction(action: MainAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    private fun updateUserAndRates() {
        viewModelScope.launch {
            updateUser(UpdateUser.Params(null, isMe = true))
                .collectInto(updatingUserDataState) { status ->
                    if (status is InvokeSuccess) updateRates()
                }
        }
    }

    private fun updateRates() {
        viewModelScope.launch {
            updateRates(Unit).collectInto(updatingUserDataState)
        }
    }

    private fun openRandomTitle() {
        viewModelScope.launch {
            listsStateManager.openRandomTitle()
        }
    }

    private fun changeListType(newType: ListType) {
        viewModelScope.launch {
            listsStateManager.updateType(newType)
            prefs.preferredListType = newType.type
        }
    }

}