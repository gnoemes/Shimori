package com.gnoemes.shimori.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.domain.PagingInteractor
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMangaWithRatePaged @Inject constructor(
    private val mangaRepository: MangaRepository,
) : PagingInteractor<ObserveMangaWithRatePaged.Params, MangaWithRate>() {

    override fun createObservable(params: Params): Flow<PagingData<MangaWithRate>> =
        Pager(config = params.pagingConfig) {
            mangaRepository.observeByStatusForPaging(params.status, params.sort)
        }.flow

    data class Params(
        val status: RateStatus,
        val sort: RateSort,
        override val pagingConfig: PagingConfig
    ) : Parameters<MangaWithRate>
}