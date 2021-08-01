package com.gnoemes.shimori.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.PagingInteractor
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.rate.ListsPage
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePagedAnimeRates @Inject constructor(
    private val animeRepository: AnimeRepository
) : PagingInteractor<ObservePagedAnimeRates.Params, AnimeWithRate>() {

    override fun createObservable(params: Params): Flow<PagingData<AnimeWithRate>> =
        Pager(config = params.pagingConfig) {
            when (params.type) {
                RateTargetType.ANIME -> animeRepository.observeByStatusForPaging(params.page.status, params.sort)
                else -> throw IllegalArgumentException()
            }
        }.flow

    data class Params(
        val type: RateTargetType,
        val page: ListsPage,
        val sort: RateSort,
        override val pagingConfig: PagingConfig
    ) : Parameters<AnimeWithRate>
}