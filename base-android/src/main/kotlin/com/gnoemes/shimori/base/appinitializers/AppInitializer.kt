package com.gnoemes.shimori.base.appinitializers

import android.app.Application

interface AppInitializer {
    fun init(app : Application)
}