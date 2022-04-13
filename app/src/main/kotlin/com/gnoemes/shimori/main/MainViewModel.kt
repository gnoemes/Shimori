package com.gnoemes.shimori.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.entities.InvokeSuccess
import com.gnoemes.shimori.common.api.UiMessage
import com.gnoemes.shimori.common.api.UiMessageManager
import com.gnoemes.shimori.common.utils.MessageID
import com.gnoemes.shimori.common.utils.ObservableLoadingCounter
import com.gnoemes.shimori.common.utils.ShimoriUiMessageTextProvider
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

    val state: StateFlow<MainViewState> =
        combine(
            listsStateManager.type.observe,
            observeHasRates.flow,
            observeShikimoriAuth.flow,
            ::MainViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = MainViewState.Empty
        )

    init {
        viewModelScope.launch {
            state
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