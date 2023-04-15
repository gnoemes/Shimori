package com.gnoemes.shimori.common.ui.navigation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import com.benasher44.uuid.uuid4

abstract class Screen : Screen {
    override val key: ScreenKey = uniqueScreenKey
    protected fun screen(provider: FeatureScreen) = ScreenRegistry.get(provider)
    protected fun tab(provider: FeatureScreen) = ScreenRegistry.get(provider) as Tab
}

//screen without global bottom navigation
abstract class BottomControlsScreen : Screen

abstract class Tab : cafe.adriel.voyager.navigator.tab.Tab {
    override val key: ScreenKey = uniqueScreenKey

    open suspend fun onReselect(navigator: Navigator) {}
    protected fun screen(provider: FeatureScreen) = ScreenRegistry.get(provider)
}

// prevents crashes in nested navigators
internal val Screen.uniqueScreenKey: ScreenKey
    get() = "Screen#${uuid4()}"