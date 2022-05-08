package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class CreateOrUpdateRate(
    private val repository: RateRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<CreateOrUpdateRate.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            repository.createOrUpdate(params.rate)
        }
    }

    data class Params(
        val rate: Rate
    )
}