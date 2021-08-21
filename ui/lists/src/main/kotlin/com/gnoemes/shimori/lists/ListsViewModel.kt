package com.gnoemes.shimori.lists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shikimori.ShikimoriAuthManager
import com.gnoemes.shimori.base.extensions.combine
import com.gnoemes.shimori.common.utils.ObservableLoadingCounter
import com.gnoemes.shimori.common.utils.collectInto
import com.gnoemes.shimori.domain.interactors.UpdateAnimeRates
import com.gnoemes.shimori.domain.interactors.UpdateMangaRates
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.domain.observers.*
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
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
    observeHasPinnedTitles: ObserveHasPinnedTitles,
    private val updateRateSort: UpdateRateSort,
    private val updateAnimeRates: UpdateAnimeRates,
    private val updateMangaRates: UpdateMangaRates,
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
                    is ListsAction.PageChanged -> updateCurrentPage(action.newPage)
                    is ListsAction.ListTypeChanged -> updateCurrentType(action.newType)
                }
            }
        }

        viewModelScope.launch {
            combine(
                    stateManager.ratesLoading.observe,
                    ratesUpdateState.observable,
                    observeShikimoriAuth.flow,
                    stateManager.type.observe,
                    observeRateSort.flow,
                    observeUser.flow,
                    observeListsPages.flow,
                    observeHasPinnedTitles.flow
            ) { globalLoading, typeLoading, auth, type, activeRateSort, user, pages, pinsExist ->
                ListsViewState(
                        loading = globalLoading || typeLoading,
                        authStatus = auth,
                        type = type,
                        user = user,
                        activeSort = activeRateSort ?: RateSort.defaultForType(type),
                        pages = pages,
                        noPinnedTitles = !pinsExist
                )
            }.collect { _state.emit(it) }
        }

        viewModelScope.launch {
            combine(
                    state.map { it.authStatus }.distinctUntilChanged(),
                    state.map { it.type }.distinctUntilChanged(),
                    stateManager.ratesLoading.observe
            ) { authState, type, loadingRates ->
                Triple(authState, type, loadingRates)
            }.collect { (authState, type, loadingRates) ->
                if (authState.isAuthorized && !loadingRates) {
                    when (type) {
                        ListType.Anime -> updateAnimeRates()
                        ListType.Manga -> updateMangaRates()
                        //TODO: add ranobe
                        else -> Unit
                    }
                }
            }
        }

        observeShikimoriAuth(Unit)
        observeUser(Unit)
        observeHasPinnedTitles(Unit)

        viewModelScope.launch {
            stateManager.type.observe.collect { type ->
                observeRateSort(ObserveRateSort.Params(type))

                type.rateType?.let {
                    observeListsPages(ObserveListsPages.Params(it))
                }
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
                            type = stateManager.type.value,
                            sort = option,
                            isDescending = isDescending)
            )
        }
    }

    private fun updateCurrentType(newType: ListType) {
        viewModelScope.launch {
            stateManager.type(newType)
        }
    }

    private fun updateCurrentPage(newPage: RateStatus) {
        viewModelScope.launch {
            stateManager.page(newPage)
        }
    }

    private fun updateAnimeRates() {
        viewModelScope.launch {
            updateAnimeRates(UpdateAnimeRates.Params.OptionalUpdate).collectInto(ratesUpdateState)
        }
    }

    private fun updateMangaRates() {
        viewModelScope.launch {
            updateMangaRates(UpdateMangaRates.Params.OptionalUpdate).collectInto(ratesUpdateState)
        }
    }
}