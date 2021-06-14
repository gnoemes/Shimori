package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRateSort @Inject constructor(
    private val rateRepository: RateRepository,
) : SubjectInteractor<ObserveRateSort.Params, RateSort?>() {

    override fun createObservable(params: Params): Flow<RateSort?> {
        return rateRepository.observeRateSort(params.type, params.status)
    }

    data class Params(
        val type: RateTargetType,
        val status: RateStatus
    )
}