package com.gnoemes.shimori.settings

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.gnoemes.shimori.base.core.di.KodeinTag
import com.gnoemes.shimori.common.ui.navigation.Feature
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

actual object SettingsFeature : Feature {
    actual override val di: DI.Module = DI.Module("settings-module") {
        bindProvider {
            SettingsScreenModel(
                instance(tag = KodeinTag.appVersion),
                instance(),
                instance(),
            )
        }
    }
    actual override val screens: ScreenRegistry.() -> Unit = screenModule {
        register<FeatureScreen.Settings> { SettingsScreen }
    }
}