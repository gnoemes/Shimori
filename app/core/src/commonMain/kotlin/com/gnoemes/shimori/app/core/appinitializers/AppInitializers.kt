package com.gnoemes.shimori.app.core.appinitializers

import com.gnoemes.shimori.base.appinitializers.AppInitializer
import me.tatarka.inject.annotations.Inject

@Inject
class AppInitializers(
    private val initializers: Set<AppInitializer>
) : AppInitializer {
    override fun init() {
        initializers.forEach { it.init() }
    }
}