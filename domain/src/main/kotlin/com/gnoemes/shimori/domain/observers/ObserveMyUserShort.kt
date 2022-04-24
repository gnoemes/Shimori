package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.base.entities.user.UserShort
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveMyUserShort(
    private val repository: ShikimoriUserRepository
) : SubjectInteractor<Unit, UserShort?>() {
    override fun create(params: Unit): Flow<UserShort?> = repository.observeMeShort()
}