package com.gnoemes.shimori.common.ui.api

import com.gnoemes.shimori.base.shared.PlatformUtils
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class UiMessage(
    val message: String,
    val action: String? = null,
    val image : ShimoriImage? = null,
    val payload: Any? = null,
    val id: Long = PlatformUtils.generateId(),
)

fun UiMessage(
    t: Throwable,
    id: Long = PlatformUtils.generateId(),
): UiMessage = UiMessage(
    message = t.message ?: "Error occurred: $t",
    id = id,
)

class UiMessageManager {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage>())

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage?> = _messages.map { it.firstOrNull() }.distinctUntilChanged()

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value = _messages.value + message
        }
    }

    suspend fun clearMessage(id: Long) {
        mutex.withLock {
            _messages.value = _messages.value.filterNot { it.id == id }
        }
    }
}
