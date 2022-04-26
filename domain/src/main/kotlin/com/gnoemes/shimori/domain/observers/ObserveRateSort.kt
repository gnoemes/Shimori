package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.base.entities.rate.ListType
import com.gnoemes.shimori.data.base.entities.rate.RateSort
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveRateSort(
    private val repository: RateRepository
) : SubjectInteractor<ObserveRateSort.Params, RateSort?>() {

    override fun create(params: Params): Flow<RateSort?> {
        return repository.observeRateSort(params.type)
    }

    data class Params(
        val type: ListType
    )
}