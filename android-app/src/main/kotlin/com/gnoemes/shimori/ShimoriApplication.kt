package com.gnoemes.shimori

import android.app.Application
import androidx.work.Configuration
import com.gnoemes.shimori.appinitializers.AppInitializers
import com.gnoemes.shimori.di.appModule
import com.gnoemes.shimori.tasks.KodeinWorkerFactory
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.instance

class ShimoriApplication : Application(), DIAware, Configuration.Provider {
    override val di: DI by DI.lazy {
        import(androidCoreModule(this@ShimoriApplication))
        import(appModule)
    }

    private val initializers: AppInitializers by di.instance()
    private val kodeinWorkerFactory: KodeinWorkerFactory by di.instance()

    override fun onCreate() {
        super.onCreate()
        initializers.init(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(kodeinWorkerFactory)
            .build()
    }
}