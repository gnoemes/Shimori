package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHasRates @Inject constructor(
    private val repository: RateRepository
) : SubjectInteractor<Unit, Boolean>() {

    override fun createObservable(params: Unit): Flow<Boolean> {
        return repository.observeHasRates()
    }
}