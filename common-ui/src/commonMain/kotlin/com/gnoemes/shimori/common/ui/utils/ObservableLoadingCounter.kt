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

package com.gnoemes.shimori.common.ui.utils

import com.gnoemes.shimori.base.core.entities.InvokeError
import com.gnoemes.shimori.base.core.entities.InvokeStarted
import com.gnoemes.shimori.base.core.entities.InvokeStatus
import com.gnoemes.shimori.base.core.entities.InvokeSuccess
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.api.UiMessageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


class ObservableLoadingCounter {
    private val count = AtomicInt()
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
    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null,
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            logger?.i(status.throwable)
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
        else -> {}
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    logger: Logger? = null,
    uiMessageManager: UiMessageManager? = null,
    onCollect: (status: InvokeStatus) -> Unit,
) = collect { status ->
    when (status) {
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
        is InvokeError -> {
            logger?.i(status.throwable)
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
        else -> {}
    }

    onCollect(status)
}
