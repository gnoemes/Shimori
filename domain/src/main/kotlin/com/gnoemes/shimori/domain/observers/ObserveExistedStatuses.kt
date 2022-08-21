package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveExistedStatuses(
    private val repository: RateRepository
) : SubjectInteractor<ObserveExistedStatuses.Params, List<RateStatus>>() {

    override fun create(params: Params): Flow<List<RateStatus>> {
        return repository.observeExistedStatuses(params.type)
    }

    data class Params(
        val type: RateTargetType
    )
}