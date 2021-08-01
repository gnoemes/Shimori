package com.gnoemes.shimori.lists

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shikimori.ShikimoriAuthManager
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
    shikimoriAuthManager: ShikimoriAuthManager,
) : ViewModel(), ShikimoriAuthManager by shikimoriAuthManager {
    private val listType: RateTargetType = RateTargetType.findOrDefault(null)

    private val pendingActions = MutableSharedFlow<ListsAction>()

    val state = MutableStateFlow(ListsViewState.Empty)

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
                    observeRateSort.observe().distinctUntilChanged(),
                    observeUser.observe().distinctUntilChanged(),
                    currentPage,
                    observeListsPages.observe().distinctUntilChanged(),
            ) { auth, activeRateSort, user, currentPage, pages ->
                ListsViewState(
                        authStatus = auth,
                        type = listType,
                        user = user,
                        activeSort = activeRateSort ?: RateSort.defaultForType(listType),
                        currentPage = currentPage,
                        pages = pages
                )
            }.collect { state.emit(it) }
        }

        observeShikimoriAuth(Unit)
        observeRateSort(ObserveRateSort.Params(listType))
        observeUser(Unit)
        observeListsPages(ObserveListsPages.Params(listType))
    }

    private fun updateSelectedPage(newPage: ListsPage) {
        currentPage.value = newPage
    }

    private fun updateRateSort(option: RateSortOption, isDescending: Boolean) {
        viewModelScope.launch {
            updateRateSort(UpdateRateSort.Params(type = listType, sort = option, isDescending = isDescending)).collect()
        }
    }

    fun submitAction(action: ListsAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }
}