package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.auth.AuthRepository
import com.gnoemes.shimori.data.auth.AuthState
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.sources.SourceIds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveShikimoriAuth(
    private val repository: AuthRepository
) : SubjectInteractor<Unit, AuthState>() {

    override fun create(params: Unit): Flow<AuthState> =
        repository.state.map { it[SourceIds.SHIKIMORI] ?: AuthState.LOGGED_OUT }
}