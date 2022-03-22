package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.domain.ResultInteractor
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleListPin @Inject constructor(
    private val pinRepository: ListPinRepository,
    private val dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ToggleListPin.Params, Boolean>() {

    override suspend fun doWork(params: Params): Boolean {
        return withContext(dispatchers.io) {
            if (params.pin != null) {
                pinRepository.pin(params.type, params.id, params.pin)
            } else {
                pinRepository.togglePin(params.type, params.id)
            }
        }
    }

    data class Params(
        val type: RateTargetType,
        val id: Long,
        val pin: Boolean? = null
    )
}