package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class DeleteRate(
    private val repository: RateRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<DeleteRate.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            repository.delete(params.id)
        }
    }

    data class Params(
        val id: Long
    )
}