package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateRateSort @Inject constructor(
    private val rateRepository: RateRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateRateSort.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val rateSort = RateSort(
                    type = params.type.type,
                    sortOption = params.sort,
                    isDescending = params.isDescending
            )
            rateRepository.updateRateSort(rateSort)
        }
    }

    data class Params(
        val type: ListType,
        val sort: RateSortOption,
        val isDescending: Boolean
    )
}