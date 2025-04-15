package com.gnoemes.shimori.domain.interactors.source

import com.gnoemes.shimori.data.auth.AuthRepository
import com.gnoemes.shimori.domain.Interactor
import me.tatarka.inject.annotations.Inject

@Inject
class LogoutSource(
    private val repository: AuthRepository,
) : Interactor<LogoutSource.Params, Unit>() {
    override suspend fun doWork(params: Params) = repository.logout(params.sourceId)


    data class Params(
        val sourceId: Long
    )
}