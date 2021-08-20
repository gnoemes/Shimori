package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveHasPinnedTitles @Inject constructor(
    private val pinRepository: ListPinRepository,
) : SubjectInteractor<Unit, Boolean>() {

    override fun createObservable(params: Unit): Flow<Boolean> = pinRepository.observeHasPins()
}