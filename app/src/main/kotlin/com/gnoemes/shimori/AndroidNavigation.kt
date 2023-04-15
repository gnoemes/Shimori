package com.gnoemes.shimori

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.gnoemes.shimori.common.ui.navigation.Feature

object AndroidNavigation {

    fun init(features: Set<@JvmSuppressWildcards Feature>) = ScreenRegistry {
        features.forEach { it.screens(this) }
    }
}