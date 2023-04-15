package com.gnoemes.shimori.auth

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.common.ui.navigation.Feature
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import org.kodein.di.DI
import org.kodein.di.bindProvider

actual object AuthFeature : Feature {
    actual override val di: DI.Module = DI.Module("auth-module") {
        bindProvider { new(::AuthScreenModel) }
    }
    actual override val screens: ScreenRegistry.() -> Unit = screenModule {
        register<FeatureScreen.Auth> { AuthScreen }
    }
}