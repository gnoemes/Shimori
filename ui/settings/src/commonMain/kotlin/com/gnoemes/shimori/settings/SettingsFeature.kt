package com.gnoemes.shimori.settings

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.gnoemes.shimori.common.ui.navigation.Feature
import org.kodein.di.DI


expect object SettingsFeature : Feature {
    override val di: DI.Module
    override val screens: ScreenRegistry.() -> Unit
}

