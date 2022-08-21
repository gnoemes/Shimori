package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObservePinsExist(
    private val repository: ListPinRepository
) : SubjectInteractor<Unit, Boolean>() {
    override fun create(params: Unit): Flow<Boolean> = repository.observePinsExist()
}