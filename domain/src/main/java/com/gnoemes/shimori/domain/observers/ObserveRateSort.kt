package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.app.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRateSort @Inject constructor(
    private val rateRepository: RateRepository,
    appCoroutineDispatchers: AppCoroutineDispatchers
) : SubjectInteractor<ObserveRateSort.Params, RateSort?>() {

    override val dispatcher: CoroutineDispatcher = appCoroutineDispatchers.io

    override fun createObservable(params: Params): Flow<RateSort?> {
        return rateRepository.observeRateSort(params.type, params.status)
    }

    data class Params(
        val type: RateTargetType,
        val status: RateStatus
    )
}