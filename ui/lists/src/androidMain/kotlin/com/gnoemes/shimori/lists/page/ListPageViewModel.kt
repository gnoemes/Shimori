package com.gnoemes.shimori.lists.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.TitleWithRate
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.list.ListsStateManager
import com.gnoemes.shimori.data.paging.PagingConfig
import com.gnoemes.shimori.data.paging.PagingData
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.observers.ObserveListPage
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListPageViewModel(
    private val stateManager: ListsStateManager,
    private val observeListPage: ObserveListPage,
    private val observeRateSort: ObserveRateSort,
    private val animeRepository: AnimeRepository,
    observeMyUser: ObserveMyUserShort
) : ViewModel() {

    val state = combine(
        stateManager.type.observe,
        stateManager.page.observe,
        observeMyUser.flow,
        ::ListPageViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ListPageViewState.Empty
    )

    val items = observeListPage.flow
        .filterIsInstance<PagingData<TitleWithRate<out ShimoriTitleEntity>>>()
        .cachedIn(viewModelScope)

    init {

        viewModelScope.launch {
            combine(
                stateManager.type.observe,
                stateManager.page.observe,
                observeRateSort.flow,
            ) { type, status, sort ->
                ObserveListPage.Params(
                    type,
                    status,
                    sort ?: RateSort.defaultForType(type),
                    PAGING_CONFIG
                )
            }
                .distinctUntilChanged()
                .collect(observeListPage::invoke)
        }

        viewModelScope.launch {
            stateManager.type.observe
                .map(ObserveRateSort::Params)
                .collect(observeRateSort::invoke)
        }

        observeMyUser(Unit)
    }


    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            prefetchDistance = 10
        )
    }
}