package com.gnoemes.shimori.appinitializers

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import javax.inject.Inject

class AppCompatInitializer @Inject constructor() : AppInitializer {

    override fun init(app: Application) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}