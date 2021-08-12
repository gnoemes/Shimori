package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observe available lists pages
 */
class ObserveListsPages @Inject constructor(
    private val repository: RateRepository
) : SubjectInteractor<ObserveListsPages.Params, List<RateStatus>>() {

    override fun createObservable(params: Params): Flow<List<RateStatus>> {
        return repository.observeListsPages(params.type)
    }

    data class Params(
        val type: RateTargetType
    )
}