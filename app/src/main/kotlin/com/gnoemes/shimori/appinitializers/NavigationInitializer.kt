package com.gnoemes.shimori.appinitializers

import android.app.Application
import com.gnoemes.shimori.AndroidNavigation
import com.gnoemes.shimori.base.core.appinitializers.AppInitializer
import com.gnoemes.shimori.common.ui.navigation.Feature

class NavigationInitializer(
    private val features : Set<@JvmSuppressWildcards Feature>
) : AppInitializer<Application> {
    override fun init(context: Application) {
        AndroidNavigation.init(features)
    }
}