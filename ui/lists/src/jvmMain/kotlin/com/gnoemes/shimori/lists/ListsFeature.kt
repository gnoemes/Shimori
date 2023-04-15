package com.gnoemes.shimori.lists

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.gnoemes.shimori.common.ui.navigation.Feature
import org.kodein.di.DI

actual object ListsFeature : Feature {
    actual override val di: DI.Module
        get() = TODO("Not yet implemented")
    actual override val screens: ScreenRegistry.() -> Unit
        get() = TODO("Not yet implemented")
}