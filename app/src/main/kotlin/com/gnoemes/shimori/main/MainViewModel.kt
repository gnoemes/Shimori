package com.gnoemes.shimori.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.entities.InvokeSuccess
import com.gnoemes.shimori.common.utils.ObservableLoadingCounter
import com.gnoemes.shimori.common.utils.collectStatus
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.interactors.UpdateRates
import com.gnoemes.shimori.domain.interactors.UpdateUser
import com.gnoemes.shimori.domain.observers.ObserveHasRates
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeShikimoriAuth: ObserveShikimoriAuth,
    private val updateUser: UpdateUser,
    private val updateRates: UpdateRates,
    private val listsStateManager: ListsStateManager,
    observeHasRates: ObserveHasRates
) : ViewModel() {

    private val updatingUserDataState = ObservableLoadingCounter()
    private val _state = MutableStateFlow(MainViewState.Empty)

    val state: StateFlow<MainViewState> get() = _state

    init {

        viewModelScope.launch {
            combine(
                listsStateManager.type.observe,
                observeHasRates.flow,
                observeShikimoriAuth.flow
            ) { listType, hasRates, authState ->
                MainViewState(
                    listType = listType,
                    hasRates = hasRates,
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
            updatingUserDataState
                .observable
                .collect { listsStateManager.ratesLoading.update(it) }
        }

        observeShikimoriAuth(Unit)
        observeHasRates(Unit)
    }

    private fun updateUserAndRates() {
        viewModelScope.launch {
            updateUser(UpdateUser.Params(null, isMe = true))
                .collectStatus(updatingUserDataState) { status ->
                    if (status is InvokeSuccess) updateRates()
                }
        }
    }

    private fun updateRates() {
        viewModelScope.launch {
            updateRates(Unit).collectStatus(updatingUserDataState)
        }
    }
}