package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.user.UserShort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyUserShort @Inject constructor(
    private val repository: UserRepository,
    appCoroutineDispatchers: AppCoroutineDispatchers
) : SubjectInteractor<Unit, UserShort?>() {

    override val dispatcher: CoroutineDispatcher = appCoroutineDispatchers.io

    override fun createObservable(params: Unit): Flow<UserShort?> = repository.observeMeShort()
}