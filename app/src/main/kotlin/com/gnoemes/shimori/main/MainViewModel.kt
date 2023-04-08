package com.gnoemes.shimori.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.common.ui.utils.ObservableLoadingCounter
import com.gnoemes.shimori.common.ui.utils.collectStatus
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.domain.interactors.UpdateUserAndTracks
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel constructor(
    observeShikimoriAuth: ObserveShikimoriAuth,
    private val listsStateBus: ListsStateBus,
    private val updateUserAndTracks: UpdateUserAndTracks,
    private val logger: Logger,
    settings: ShimoriSettings
) : ViewModel() {

    private val updatingUserDataState = ObservableLoadingCounter()

    val settingsState: StateFlow<MainSettingsViewState> =
        combine(
            settings.titlesLocale.observe,
            settings.locale.observe,
            ::MainSettingsViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainSettingsViewState(
                AppTitlesLocale.English,
                AppLocale.English
            )
        )

    init {
        viewModelScope.launch {
            observeShikimoriAuth.flow
                .collect {
                    if (it.isAuthorized) updateUserAndTracks()
                }
        }

        viewModelScope.launch {
            updatingUserDataState
                .observable
                .collect { listsStateBus.tracksLoading.update(it) }
        }


        observeShikimoriAuth(Unit)
    }

    private fun updateUserAndTracks() {
        viewModelScope.launch {
            updateUserAndTracks.invoke(Unit)
                .collectStatus(updatingUserDataState, logger = logger)
        }
    }
}