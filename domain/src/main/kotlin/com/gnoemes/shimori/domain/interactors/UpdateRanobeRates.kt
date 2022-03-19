package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateRanobeRates @Inject constructor(
    private val repository: RanobeRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateRanobeRates.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) {
                repository.updateMyRanobeWithStatus(params.status)
            } else if (params.optionalUpdate && repository.needUpdateRanobeWithStatus(params.status)) {
                repository.updateMyRanobeWithStatus(params.status)
            }
        }
    }

    data class Params(
        val force: Boolean,
        val optionalUpdate: Boolean,
        val status: RateStatus?
    ) {
        companion object {
            val OptionalUpdate = Params(force = false, optionalUpdate = true, status = null)
        }
    }
}