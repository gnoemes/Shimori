package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateSortOption
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateRateSort(
    private val repository: RateRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateRateSort.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val sort = RateSort(
                type = params.type,
                sortOption = params.sort,
                isDescending = params.descending
            )
            repository.createOrUpdate(sort)
        }
    }

    data class Params(
        val type: ListType,
        val sort: RateSortOption,
        val descending: Boolean
    )
}