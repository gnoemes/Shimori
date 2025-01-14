package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.user.UserRepository
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.sources.SourceIds
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveMyUserShort(
    private val repository: UserRepository
) : SubjectInteractor<Unit, UserShort?>() {
    override fun create(params: Unit): Flow<UserShort?> =
        //TODO support several sources
        repository.observeMeShort(SourceIds.SHIKIMORI)
}