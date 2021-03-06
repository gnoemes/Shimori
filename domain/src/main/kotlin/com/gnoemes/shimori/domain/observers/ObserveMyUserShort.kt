package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.user.UserShort
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyUserShort @Inject constructor(
    private val repository: UserRepository,
) : SubjectInteractor<Unit, UserShort?>() {

    override fun createObservable(params: Unit): Flow<UserShort?> = repository.observeMeShort()
}