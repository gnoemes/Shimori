package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRates @Inject constructor(
    private val rateRepository: RateRepository,
    dispatchers: AppCoroutineDispatchers
) : SubjectInteractor<ObserveRates.Params, List<Rate>>() {

    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<List<Rate>> {
        return rateRepository.observeRates(params.type)
    }

    data class Params(
        val type: RateTargetType
    )
}