package com.gnoemes.shimori.lists.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.observers.ObserveAnimeWithRatePaged
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPageViewModel @Inject constructor(
    private val pagingInteractor: ObserveAnimeWithRatePaged,
    private val listManager: ListsStateManager,
    private val observeRateSort: ObserveRateSort,
) : ViewModel() {

    val pagedList = pagingInteractor.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            combine(
                listManager.page.observe,
                observeRateSort.flow,
            ) { status, sort ->
                ObserveAnimeWithRatePaged.Params(
                    status = status,
                    sort = sort ?: RateSort.defaultForType(ListType.Anime),
                    pagingConfig = PAGING_CONFIG
                )
            }.collect { pagingInteractor(it) }
        }

        viewModelScope.launch {
            listManager.type.observe
                .map(ObserveRateSort::Params)
                .collect(observeRateSort::invoke)
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 50,
            initialLoadSize = 50
        )
    }
}