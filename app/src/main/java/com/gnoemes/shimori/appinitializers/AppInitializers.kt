package com.gnoemes.shimori.appinitializers

import android.app.Application
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import javax.inject.Inject

class AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer>
) {

    fun init(app: Application) {
        initializers.forEach {
            it.init(app)
        }
    }
}