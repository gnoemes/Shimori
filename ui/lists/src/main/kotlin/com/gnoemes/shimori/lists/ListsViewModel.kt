package com.gnoemes.shimori.lists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shikimori.ShikimoriAuthManager
import com.gnoemes.shimori.base.extensions.combine
import com.gnoemes.shimori.common.utils.ObservableLoadingCounter
import com.gnoemes.shimori.common.utils.collectInto
import com.gnoemes.shimori.domain.interactors.UpdateAnimeRates
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.domain.observers.ObserveListsPages
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.model.rate.ListsPage
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ListsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeShikimoriAuth: ObserveShikimoriAuth,
    observeRateSort: ObserveRateSort,
    observeListsPages: ObserveListsPages,
    observeUser: ObserveMyUserShort,
    private val updateRateSort: UpdateRateSort,
    private val updateAnimeRates: UpdateAnimeRates,
    shikimoriAuthManager: ShikimoriAuthManager,
    private val stateManager: ListsStateManager
) : ViewModel(), ShikimoriAuthManager by shikimoriAuthManager {
    private val ratesUpdateState = ObservableLoadingCounter()
    private val pendingActions = MutableSharedFlow<ListsAction>()

    private val _state = MutableStateFlow(ListsViewState.Empty)

    val state: StateFlow<ListsViewState> get() = _state

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is ListsAction.UpdateListSort -> updateRateSort(action.option, action.isDescending)
                    is ListsAction.UpdateCurrentPage -> updateCurrentPage(action.page)
                }

            }
        }

        viewModelScope.launch {
            combine(
                    stateManager.updatingRates,
                    ratesUpdateState.observable,
                    observeShikimoriAuth.observe().distinctUntilChanged(),
                    stateManager.currentType,
                    observeRateSort.observe().distinctUntilChanged(),
                    observeUser.observe().distinctUntilChanged(),
                    observeListsPages.observe().distinctUntilChanged(),
            ) { globalLoading, typeLoading, auth, type, activeRateSort, user, pages ->
                ListsViewState(
                        loading = globalLoading || typeLoading,
                        authStatus = auth,
                        type = type,
                        user = user,
                        activeSort = activeRateSort ?: RateSort.defaultForType(type),
                        pages = pages
                )
            }.collect { _state.emit(it) }
        }

        viewModelScope.launch {
            combine(
                    state.map { it.authStatus }.distinctUntilChanged(),
                    state.map { it.type }.distinctUntilChanged(),
                    stateManager.updatingRates
            ) { authState, type, loadingRates -> Triple(authState, type, loadingRates)
            }.collect { (authState, type, loadingRates) ->
                if (authState.isAuthorized && !loadingRates) {
                    when(type) {
                        RateTargetType.ANIME -> updateAnimeRates()
                        //TODO: add manga
                        else -> Unit
                    }
                }
            }
        }

        observeShikimoriAuth(Unit)
        observeUser(Unit)

        viewModelScope.launch {
            stateManager.currentType.collect { type ->
                observeRateSort(ObserveRateSort.Params(type))
                observeListsPages(ObserveListsPages.Params(type))
            }
        }
    }

    fun submitAction(action: ListsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    private fun updateRateSort(option: RateSortOption, isDescending: Boolean) {
        viewModelScope.launch {
            updateRateSort.executeSync(
                    UpdateRateSort.Params(
                            type = stateManager.currentType.value,
                            sort = option,
                            isDescending = isDescending)
            )
        }
    }

    private fun updateCurrentPage(page: ListsPage) {
        viewModelScope.launch {
            stateManager.updateCurrentPage(page)
        }
    }

    private fun updateAnimeRates() {
        viewModelScope.launch {
            updateAnimeRates(UpdateAnimeRates.Params.OptionalUpdate).collectInto(ratesUpdateState)
        }
    }
}