package com.gnoemes.shimori.appinitializers

import android.app.Application
import com.gnoemes.shimori.base.core.appinitializers.AppInitializer

class AppInitializers constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer<Application>>
) {

    fun init(app: Application) {
        initializers.forEach {
            it.init(app)
        }
    }
}