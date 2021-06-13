package com.gnoemes.shimori.appinitializers

import android.app.Application
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import javax.inject.Inject

class PreferencesInitializer @Inject constructor(
    private val prefs: ShimoriPreferences
) : AppInitializer {
    override fun init(app: Application) {
        prefs.setup()
    }
}