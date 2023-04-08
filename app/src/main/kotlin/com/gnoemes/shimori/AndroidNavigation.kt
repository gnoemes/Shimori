package com.gnoemes.shimori

import cafe.adriel.voyager.core.registry.ScreenRegistry
import com.gnoemes.shimori.home.HomeFeature

object AndroidNavigation {
    fun init() = ScreenRegistry {
        HomeFeature.screens(this)
    }
}