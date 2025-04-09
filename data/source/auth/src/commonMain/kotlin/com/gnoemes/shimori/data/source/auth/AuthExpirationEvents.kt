package com.gnoemes.shimori.data.source.auth

import com.gnoemes.shimori.source.auth.AuthExpiration
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = AuthExpiration::class)
class AuthExpirationEvents : AuthExpiration {
    private val _events = MutableSharedFlow<Long>()
    val events: SharedFlow<Long> = _events.asSharedFlow()

    override suspend fun onAuthExpired(sourceId: Long) {
        _events.emit(sourceId)
    }
}