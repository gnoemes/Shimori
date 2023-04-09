package com.gnoemes.shimori.lists.change

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.gnoemes.shimori.common.ui.navigation.Feature
import org.kodein.di.DI

expect object ListMenuFeature : Feature {
    override val di: DI.Module
    override val screens: ScreenRegistry.() -> Unit
}