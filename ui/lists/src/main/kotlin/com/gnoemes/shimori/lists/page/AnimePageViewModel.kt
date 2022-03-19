package com.gnoemes.shimori.lists.page

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.observers.ObserveAnimeWithRatePaged
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AnimePageViewModel @Inject constructor(
    private val pagingInteractor: ObserveAnimeWithRatePaged,
    listManager: ListsStateManager,
    observeMy: ObserveMyUserShort,
    observeRateSort: ObserveRateSort,
    getRandomTitle: GetRandomTitleWithStatus,
) : BasePageViewModel(listManager, observeMy, observeRateSort, getRandomTitle) {

    override val listType = ListType.Anime
    val pagedList = pagingInteractor.flow.cachedIn(viewModelScope)

    override suspend fun loadPage(status: RateStatus, sort: RateSort) {
        pagingInteractor(
            ObserveAnimeWithRatePaged.Params(
                status = status,
                sort = sort,
                pagingConfig = PAGING_CONFIG
            )
        )
    }

}