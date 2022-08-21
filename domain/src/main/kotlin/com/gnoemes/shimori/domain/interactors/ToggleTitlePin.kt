package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.domain.ResultInteractor
import kotlinx.coroutines.withContext

class ToggleTitlePin(
    private val repository: ListPinRepository,
    private val dispatchers: AppCoroutineDispatchers
) : ResultInteractor<ToggleTitlePin.Params, Boolean>() {

    override suspend fun doWork(params: Params): Boolean {
        return withContext(dispatchers.io) {
            if (params.pin != null) {
                repository.pin(params.id, params.type, params.pin)
            } else {
                repository.togglePin(params.id, params.type)
            }
        }
    }

    data class Params(
        val type: RateTargetType,
        val id: Long,
        val pin: Boolean? = null
    )
}