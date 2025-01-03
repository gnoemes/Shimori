package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.data.auth.AuthRepository
import com.gnoemes.shimori.data.source.SourceIds
import com.gnoemes.shimori.domain.Interactor
import me.tatarka.inject.annotations.Inject

@Inject
class SignInShikimori(
    private val repository: AuthRepository,
) : Interactor<Unit, Unit>() {
    override suspend fun doWork(params: Unit) = repository.signIn(SourceIds.SHIKIMORI)
}