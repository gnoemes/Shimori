package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateRates(
    private val rateRepository: RateRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            rateRepository.sync()
        }
    }

}