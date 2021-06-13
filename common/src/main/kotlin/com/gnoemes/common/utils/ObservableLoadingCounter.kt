package com.gnoemes.common.utils

import com.gnoemes.shimori.base.entities.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = ConflatedBroadcastChannel(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.asFlow().map { it > 0 }

    fun addLoader() {
        loadingState.sendBlocking(count.incrementAndGet())
    }

    fun removeLoader() {
        loadingState.sendBlocking(count.decrementAndGet())
    }
}

suspend fun ObservableLoadingCounter.collectFrom(statuses: Flow<InvokeStatus>) {
    statuses.collect {
        if (it == InvokeStarted) {
            addLoader()
        }
        else if (it == InvokeSuccess || it == InvokeTimeout || it is InvokeError) {
            removeLoader()
        }
    }
}