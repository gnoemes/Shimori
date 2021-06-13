package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.rate.Rate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRate @Inject constructor(
    private val rateRepository: RateRepository,
    dispatches: AppCoroutineDispatchers
) : SubjectInteractor<ObserveRate.Params, Rate?>() {

    override val dispatcher: CoroutineDispatcher = dispatches.io

    override fun createObservable(params: Params): Flow<Rate?> =
        rateRepository.observeRate(params.shikimoriId)

    data class Params(
        val shikimoriId: Long
    )
}