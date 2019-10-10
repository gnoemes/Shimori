package com.gnoemes.shimori.appinitializers

import android.app.Application
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Inject

class JodaTimeInitializer @Inject constructor(): AppInitializer {

    override fun init(app: Application) {
        JodaTimeAndroid.init(app)
    }
}