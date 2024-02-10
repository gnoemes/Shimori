package com.gnoemes.shimori.screens

import com.slack.circuit.runtime.screen.Screen

@CommonParcelize
object ListsScreen : ShimoriScreen("Lists()")

@CommonParcelize
object MockScreen : ShimoriScreen("Mock()")

abstract class ShimoriScreen(val name: String) : Screen {
    open val arguments: Map<String, *>? = null
}