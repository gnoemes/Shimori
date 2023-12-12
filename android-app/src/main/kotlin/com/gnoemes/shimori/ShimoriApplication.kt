package com.gnoemes.shimori

import android.app.Application

class ShimoriApplication : Application() {
    val component: AndroidApplicationComponent by lazy {
        AndroidApplicationComponent.create(this)
    }

    override fun onCreate() {
        super.onCreate()
//        component.initializers.init()
    }
}