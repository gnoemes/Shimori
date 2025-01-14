package com.gnoemes.shimori.data.source.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class AuthExpirationEvents {
    private val _events = MutableSharedFlow<Long>()
    val events: SharedFlow<Long> = _events.asSharedFlow()

    suspend fun onAuthExpired(sourceId: Long) {
        _events.emit(sourceId)
    }
}