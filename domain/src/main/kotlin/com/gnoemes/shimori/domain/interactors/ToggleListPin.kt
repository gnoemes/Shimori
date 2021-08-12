package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleListPin @Inject constructor(
    private val pinRepository: ListPinRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<ToggleListPin.Params>() {


    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            pinRepository.togglePin(params.type, params.shikimoriId)
        }
    }

    data class Params(
        val type : RateTargetType,
        val shikimoriId : Long
    )
}