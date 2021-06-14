package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.Rate
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteRate @Inject constructor(
    private val repository: RateRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<DeleteRate.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            repository.deleteRate(params.rate)
        }
    }

    data class Params(
        val rate: Rate
    )
}