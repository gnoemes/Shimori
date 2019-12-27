package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.di.ProcessLifetime
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateRates @Inject constructor(
    private val userRepository: UserRepository,
    private val rateRepository: RateRepository,
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime val processScope: CoroutineScope
) : Interactor<Unit>(){
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Unit) {
        rateRepository.syncRates()
    }

}