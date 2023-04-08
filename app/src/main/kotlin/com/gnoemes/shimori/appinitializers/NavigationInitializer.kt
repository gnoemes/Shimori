package com.gnoemes.shimori.appinitializers

import android.app.Application
import com.gnoemes.shimori.AndroidNavigation
import com.gnoemes.shimori.base.core.appinitializers.AppInitializer

class NavigationInitializer : AppInitializer<Application> {
    override fun init(context: Application) {
        AndroidNavigation.init()
    }
}