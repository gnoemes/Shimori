package com.gnoemes.shimori.title

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.gnoemes.shimori.common.ui.navigation.Feature
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

actual object TitleDetailsFeature : Feature {
    actual override val di: DI.Module = DI.Module("title-module") {
        bindFactory { args: FeatureScreen.TitleDetails ->
            TitleDetailsScreenModel(
                args,
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
            )
        }
    }
    actual override val screens: ScreenRegistry.() -> Unit = screenModule {
        register<FeatureScreen.TitleDetails> { TitleDetailsScreen(it) }
    }
}