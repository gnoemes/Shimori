package com.gnoemes.shimori.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.domain.PagingInteractor
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAnimeWithRatePaged @Inject constructor(
    private val animeRepository: AnimeRepository,
) : PagingInteractor<ObserveAnimeWithRatePaged.Params, AnimeWithRate>() {

    override fun createObservable(params: Params): Flow<PagingData<AnimeWithRate>> =
        Pager(config = params.pagingConfig) {
            animeRepository.observeByStatusForPaging(params.status, params.sort)
        }.flow

    data class Params(
        val status: RateStatus,
        val sort: RateSort,
        override val pagingConfig: PagingConfig
    ) : Parameters<AnimeWithRate>
}