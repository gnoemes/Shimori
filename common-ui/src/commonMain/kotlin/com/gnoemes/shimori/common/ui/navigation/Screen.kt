package com.gnoemes.shimori.common.ui.navigation

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.benasher44.uuid.uuid4

abstract class Screen : Screen {
    override val key: ScreenKey = uniqueScreenKey
}

// prevents crashes in nested navigators
internal val Screen.uniqueScreenKey: ScreenKey
    get() = "Screen#${uuid4()}"