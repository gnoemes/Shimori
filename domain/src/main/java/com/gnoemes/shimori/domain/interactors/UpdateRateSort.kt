package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.di.ProcessLifetime
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateRateSort @Inject constructor(
    private val rateRepository: RateRepository,
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime val processScope: CoroutineScope
) : Interactor<UpdateRateSort.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        val rateSort = RateSort(
                type = params.type,
                status = params.status,
                sortOption = params.sort,
                isDescending = params.isDescending
        )
        rateRepository.updateRateSort(rateSort)
    }

    data class Params(
        val type: RateTargetType,
        val status: RateStatus,
        val sort: RateSortOption,
        val isDescending: Boolean
    )
}