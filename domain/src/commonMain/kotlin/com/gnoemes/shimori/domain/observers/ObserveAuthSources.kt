package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.auth.AuthRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.source.Source
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveAuthSources(
    private val repository: AuthRepository
) : SubjectInteractor<Unit, List<Source>>() {

    override fun create(params: Unit): Flow<List<Source>> = repository.observeAuthSources()
}