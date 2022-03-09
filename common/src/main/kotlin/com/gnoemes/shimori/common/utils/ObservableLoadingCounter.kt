/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gnoemes.shimori.common.utils

import android.util.Log
import com.gnoemes.shimori.base.entities.InvokeError
import com.gnoemes.shimori.base.entities.InvokeStarted
import com.gnoemes.shimori.base.entities.InvokeStatus
import com.gnoemes.shimori.base.entities.InvokeSuccess
import com.gnoemes.shimori.common.api.UiMessage
import com.gnoemes.shimori.common.api.UiMessageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null,
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            Log.i("DEVE", "err ${status.throwable}")
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
        else -> {}
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null,
    onCollect : (status : InvokeStatus) -> Unit,
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            Log.i("DEVE", "err ${status.throwable}")
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
        else -> {}
    }

    onCollect(status)
}
