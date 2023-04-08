package com.gnoemes.shimori.common.ui.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class FeatureScreen : ScreenProvider {
    object Home : FeatureScreen()
}
