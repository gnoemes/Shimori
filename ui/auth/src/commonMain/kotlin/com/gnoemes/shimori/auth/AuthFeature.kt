package com.gnoemes.shimori.auth

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.gnoemes.shimori.common.ui.navigation.Feature
import org.kodein.di.DI


expect object AuthFeature : Feature {
    override val di: DI.Module
    override val screens: ScreenRegistry.() -> Unit
}

