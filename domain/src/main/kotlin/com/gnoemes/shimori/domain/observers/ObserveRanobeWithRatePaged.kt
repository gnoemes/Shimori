package com.gnoemes.shimori.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.PagingInteractor
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRanobeWithRatePaged @Inject constructor(
    private val ranobeRepository: RanobeRepository,
) : PagingInteractor<ObserveRanobeWithRatePaged.Params, RanobeWithRate>() {

    override fun createObservable(params: Params): Flow<PagingData<RanobeWithRate>> =
        Pager(config = params.pagingConfig) {
            ranobeRepository.observeByStatusForPaging(params.status, params.sort)
        }.flow

    data class Params(
        val status: RateStatus,
        val sort: RateSort,
        override val pagingConfig: PagingConfig
    ) : Parameters<RanobeWithRate>
}