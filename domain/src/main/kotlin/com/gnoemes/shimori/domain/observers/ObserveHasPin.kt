package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHasPin @Inject constructor(
    private val pinRepository: ListPinRepository,
) : SubjectInteractor<ObserveHasPin.Params, Boolean>() {

    override fun createObservable(params: Params): Flow<Boolean> {
        return pinRepository.observeHasPin(params.targetId, params.targetType)
    }

    data class Params(
        val targetId: Long,
        val targetType: RateTargetType
    )
}