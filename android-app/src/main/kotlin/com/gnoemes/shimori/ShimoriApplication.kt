package com.gnoemes.shimori

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkerFactory

class ShimoriApplication : Application(), Configuration.Provider {
    val component: AndroidApplicationComponent by lazy {
        AndroidApplicationComponent.create(this)
    }

    private lateinit var workerFactory: WorkerFactory

    override fun onCreate() {
        super.onCreate()
        workerFactory = component.workerFactory
        component.initializers.init()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}