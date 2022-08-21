package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveRatesExist constructor(
    private val repository: RateRepository
) : SubjectInteractor<Unit, Boolean>() {

    override fun create(params: Unit): Flow<Boolean> {
        return repository.observeRatesExist()
    }
}