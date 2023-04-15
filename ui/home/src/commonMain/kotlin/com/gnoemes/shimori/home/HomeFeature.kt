package com.gnoemes.shimori.home

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.gnoemes.shimori.common.ui.navigation.Feature
import org.kodein.di.DI


expect object HomeFeature : Feature {
    override val di: DI.Module
    override val screens: ScreenRegistry.() -> Unit
}

