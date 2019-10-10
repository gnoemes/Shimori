package com.gnoemes.shimori.di

import android.content.Context
import android.os.Build
import com.gnoemes.shimori.BuildConfig
import com.gnoemes.shimori.ShimoriApplication
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModuleBinds::class])
class AppModule {

    @Provides
    fun provideContext(app: ShimoriApplication): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideCoroutineDispatchers() = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Singleton
    @Provides
    fun provideBackgroundExecutor(): Executor {
        val parallelism = (Runtime.getRuntime().availableProcessors() * 2)
            .coerceIn(4, 32)

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Executors.newFixedThreadPool(parallelism)
        } else {
            Executors.newWorkStealingPool(parallelism)
        }
    }

    @Provides
    @Singleton
    @Named("cache")
    fun provideCacheDir(app: ShimoriApplication): File = app.cacheDir

    @Provides
    @Named("shimori-client-id")
    fun provideShimoriClientId(): String = BuildConfig.ShimoriClientId

    @Provides
    @Named("shimori-secret-key")
    fun provideShimoriSecretKey() : String = BuildConfig.ShimoriClientSecret

}