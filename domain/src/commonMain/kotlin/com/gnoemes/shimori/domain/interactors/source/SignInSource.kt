package com.gnoemes.shimori.domain.interactors.source

import com.gnoemes.shimori.data.auth.AuthRepository
import com.gnoemes.shimori.domain.Interactor
import me.tatarka.inject.annotations.Inject

@Inject
class SignInSource(
    private val repository: AuthRepository,
) : Interactor<SignInSource.Params, Unit>() {
    override suspend fun doWork(params: Params) = repository.signIn(params.sourceId)


    data class Params(
        val sourceId: Long
    )
}