package com.gnoemes.shimori.screens

@Suppress("UNUSED_EXPRESSION")
actual fun overlayTypeFor(screen: OverlayScreen) = when (screen) {
    else -> OverlayType.None
}