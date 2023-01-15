package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.repositories.source.SourceRepository
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveMyUserShort(
    private val sourceRepository: SourceRepository,
    private val repository: UserRepository
) : SubjectInteractor<Unit, UserShort?>() {
    override fun create(params: Unit): Flow<UserShort?> =
        repository.observeMeShort(sourceRepository.currentCatalog.id)
}