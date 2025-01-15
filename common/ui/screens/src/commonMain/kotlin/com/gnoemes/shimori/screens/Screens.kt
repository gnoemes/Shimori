package com.gnoemes.shimori.screens

import com.slack.circuit.runtime.screen.Screen

@Parcelize
object HomeScreen : ShimoriScreen("Home()")

@Parcelize
object TracksScreen : ShimoriScreen("Tracks()")

@Parcelize
object AuthScreen : ShimoriScreen("Auth()")

@Parcelize
object SettingsScreen : ShimoriScreen("Settings()"), OverlayScreen

@Parcelize
data class SettingsAppearanceScreen(val card: Boolean = false) : ShimoriScreen("SettingsAppearance()") {
    override val arguments get() = mapOf("card" to card)
}

@Parcelize
object MockScreen : ShimoriScreen("Mock()")

@Parcelize
data class UrlScreen(val url: String) : ShimoriScreen(name = "UrlScreen()") {
    override val arguments get() = mapOf("url" to url)
}

abstract class ShimoriScreen(val name: String) : Screen {
    open val arguments: Map<String, *>? = null
}

sealed interface OverlayScreen

enum class OverlayType {
    None,
    Dialog,
    BottomSheet,
    SideSheet
}

expect fun overlayTypeFor(screen: OverlayScreen): OverlayType