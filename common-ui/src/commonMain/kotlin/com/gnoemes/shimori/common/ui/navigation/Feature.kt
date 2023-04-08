package com.gnoemes.shimori.common.ui.navigation

import cafe.adriel.voyager.core.registry.ScreenRegistry
import org.kodein.di.DI

interface Feature {
    val di : DI.Module
    val screens : ScreenRegistry.() -> Unit
}