package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.di.ProcessLifetime
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.Rate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class DeleteRate @Inject constructor(
    private val repository: RateRepository,
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime val processScope: CoroutineScope
) : Interactor<DeleteRate.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        repository.deleteRate(params.rate)
    }

    data class Params(
        val rate: Rate
    )
}