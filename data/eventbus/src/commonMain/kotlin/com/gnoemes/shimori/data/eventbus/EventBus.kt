package com.gnoemes.shimori.data.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance

object EventBus {
    private val _events = MutableSharedFlow<Any>()
    val events = _events.asSharedFlow()

    suspend fun publish(event: Any) = _events.emit(event)
    inline fun <reified T> observe() = events.filterIsInstance<T>()
    suspend inline fun <reified T> observe(noinline onEvent: suspend (T) -> Unit) {
        observe<T>().collect(onEvent)
    }

}