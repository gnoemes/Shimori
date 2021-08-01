package com.gnoemes.shimori.lists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shikimori.ShikimoriAuthManager
import com.gnoemes.shimori.base.extensions.combine
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.domain.observers.ObserveListsPages
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.model.rate.ListsPage
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
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
    shikimoriAuthManager: ShikimoriAuthManager,
    private val stateManager: ListsStateManager
) : ViewModel(), ShikimoriAuthManager by shikimoriAuthManager {
    private val pendingActions = MutableSharedFlow<ListsAction>()

    private val _state = MutableStateFlow(ListsViewState.Empty)

    val state : StateFlow<ListsViewState> get() = _state

    private val currentPage = MutableStateFlow(ListsPage.WATCHING)

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is ListsAction.UpdateListSort -> updateRateSort(action.option, action.isDescending)
                    is ListsAction.PageSelected -> updateSelectedPage(action.newPage)
                }

            }
        }

        viewModelScope.launch {
            combine(
                    observeShikimoriAuth.observe().distinctUntilChanged(),
                    stateManager.currentType,
                    observeRateSort.observe().distinctUntilChanged(),
                    observeUser.observe().distinctUntilChanged(),
                    currentPage,
                    observeListsPages.observe().distinctUntilChanged(),
            ) { auth, type, activeRateSort, user, currentPage, pages ->
                ListsViewState(
                        authStatus = auth,
                        type = type,
                        user = user,
                        activeSort = activeRateSort ?: RateSort.defaultForType(type),
                        currentPage = currentPage,
                        pages = pages
                )
            }.collect { _state.emit(it) }
        }

        observeShikimoriAuth(Unit)
        observeUser(Unit)

        viewModelScope.launch {
            stateManager.currentType.collect { type->
                observeRateSort(ObserveRateSort.Params(type))
                observeListsPages(ObserveListsPages.Params(type))
            }
        }
    }

    private fun updateSelectedPage(newPage: ListsPage) {
        currentPage.value = newPage
    }

    private fun updateRateSort(option: RateSortOption, isDescending: Boolean) {
        viewModelScope.launch {
            updateRateSort(UpdateRateSort.Params(type = stateManager.currentType.value, sort = option, isDescending = isDescending)).collect()
        }
    }

    fun submitAction(action: ListsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}