package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObservePinExist(
    private val repository: ListPinRepository
) : SubjectInteractor<ObservePinExist.Params, Boolean>() {

    override fun create(params: Params): Flow<Boolean> {
        return repository.observePinExist(params.targetId, params.targetType)
    }

    data class Params(
        val targetId: Long,
        val targetType: RateTargetType
    )
}