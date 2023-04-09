package com.gnoemes.shimori.lists.edit

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.gnoemes.shimori.common.ui.navigation.Feature
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

actual object TrackEditFeature : Feature {
    actual override val di: DI.Module = DI.Module("lists-edit-module") {
        bindFactory { args: FeatureScreen.TrackEdit ->
            TrackEditScreenModel(
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
        register<FeatureScreen.TrackEdit> { args ->
            TrackEditScreen(args)
        }
    }
}