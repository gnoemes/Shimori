package com.gnoemes.shimori

import android.app.Application
import com.gnoemes.shimori.appinitializers.AppInitializers
import com.gnoemes.shimori.di.appModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.instance

class ShimoriApplication : Application(), DIAware {
    override val di: DI by DI.lazy {
        import(androidCoreModule(this@ShimoriApplication))
        import(appModule)
    }

    private val initializers: AppInitializers by di.instance()

    override fun onCreate() {
        super.onCreate()
        initializers.init(this)
    }
}