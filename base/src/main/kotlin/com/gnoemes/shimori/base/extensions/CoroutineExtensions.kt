package com.gnoemes.shimori.base.extensions

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

const val DEFERREDS_BEFORE_CLEAR_SIZE = 100

val deferreds = ConcurrentHashMap<Any, Deferred<*>>()
val deferredsCleanLaunched = AtomicBoolean()

suspend inline fun <T> asyncOrAwait(
    key: Any,
    crossinline action: suspend CoroutineScope.() -> T
): T = coroutineScope {
    val deferred = deferreds[key]?.takeIf { it.isActive }
        ?: async { action() }
            .also { deferreds[key] = it }

    if (deferreds.size > DEFERREDS_BEFORE_CLEAR_SIZE && !deferredsCleanLaunched.getAndSet(true)) {
        launch {
            // Remove any complete entries
            deferreds.entries.removeAll { it.value.isCompleted }
            deferredsCleanLaunched.set(false)
        }
    }

    @Suppress("UNCHECKED_CAST")
    deferred.await() as T
}