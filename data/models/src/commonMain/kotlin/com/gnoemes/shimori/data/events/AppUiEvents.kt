package com.gnoemes.shimori.data.events

import com.benasher44.uuid.uuid4

sealed class AppUiEvents(
    val navigation: Boolean = false,
    val eventId: Long = uuid4().mostSignificantBits
) {
    data class UiError(
        val error: Throwable,
    ) : AppUiEvents()

    data class UiMessage(
        val message: String,
    ) : AppUiEvents()
}