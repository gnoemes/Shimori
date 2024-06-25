package com.gnoemes.shimori.screens

import com.slack.circuit.runtime.screen.Screen

@Parcelize
object ListsScreen : ShimoriScreen("Lists()")

@Parcelize
object MockScreen : ShimoriScreen("Mock()")

@Parcelize
data class UrlScreen(val url: String) : ShimoriScreen(name = "UrlScreen()") {
    override val arguments get() = mapOf("url" to url)
}

abstract class ShimoriScreen(val name: String) : Screen {
    open val arguments: Map<String, *>? = null
}