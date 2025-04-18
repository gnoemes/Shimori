package com.gnoemes.shimori.data.events

import com.benasher44.uuid.uuid4

sealed class TitleUiEvents(
    val navigation: Boolean = false,
    val eventId: Long = uuid4().mostSignificantBits
) {
    data object HideCharacters : TitleUiEvents()
    data object HideTrailers : TitleUiEvents()
}