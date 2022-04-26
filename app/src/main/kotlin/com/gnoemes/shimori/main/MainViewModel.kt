package com.gnoemes.shimori.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.entities.InvokeSuccess
import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.common.ui.utils.ObservableLoadingCounter
import com.gnoemes.shimori.common.ui.utils.collectStatus
import com.gnoemes.shimori.data.list.ListsStateManager
import com.gnoemes.shimori.domain.interactors.UpdateRates
import com.gnoemes.shimori.domain.interactors.UpdateUser
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel constructor(
    observeShikimoriAuth: ObserveShikimoriAuth,
    private val listsStateManager: ListsStateManager,
    private val updateUser: UpdateUser,
    private val updateRates: UpdateRates,
    private val logger: Logger,
    settings: ShimoriSettings
) : ViewModel() {

    private val updatingUserDataState = ObservableLoadingCounter()

    val state: StateFlow<MainViewState> = listsStateManager.type.observe
        .map(::MainViewState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = MainViewState.Empty
        )

    val settingsState: StateFlow<MainSettingsViewState> =
        combine(
            settings.titlesLocale.observe,
            settings.locale.observe,
            ::MainSettingsViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = MainSettingsViewState(
                AppTitlesLocale.English,
                AppLocale.English
            )
        )

    init {
        viewModelScope.launch {
            observeShikimoriAuth.flow
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
    }

    private fun updateUserAndRates() {
        viewModelScope.launch {
            updateUser(UpdateUser.Params(null, isMe = true))
                .collectStatus(updatingUserDataState, logger = logger) { status ->
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