package com.gnoemes.shimori.home

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.gnoemes.shimori.common.ui.navigation.Feature
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.new

actual object HomeFeature : Feature {
    actual override val di: DI.Module = DI.Module("home-module") {
        bindProvider { new(::HomeScreenModel) }
    }
    actual override val screens: ScreenRegistry.() -> Unit = screenModule {
        register<FeatureScreen.Home> { HomeScreen }
    }
}