package com.gnoemes.shimori.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.gnoemes.shikimori.ShikimoriModule
import com.gnoemes.shimori.BuildConfig
import com.gnoemes.shimori.base.di.MediumDate
import com.gnoemes.shimori.base.di.MediumDateTime
import com.gnoemes.shimori.base.di.ShortDate
import com.gnoemes.shimori.base.di.ShortTime
import com.gnoemes.shimori.base.extensions.withLocale
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [
    AppModuleBinds::class,
    ShikimoriModule::class
])
object AppModule {

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
    fun provideCacheDir(app: Application): File = app.cacheDir

    @Provides
    @Named("shikimori-client-id")
    fun provideShimoriClientId(): String = BuildConfig.ShikimoriClientId

    @Provides
    @Named("shikimori-secret-key")
    fun provideShimoriSecretKey(): String = BuildConfig.ShikimoriClientSecret

    @Singleton
    @Provides
    @ShortDate
    fun provideShortDateFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(context)
    }

    @Singleton
    @Provides
    @ShortTime
    fun provideShortTimeFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(context)
    }

    @Singleton
    @Provides
    @MediumDate
    fun provideMediumDateFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @MediumDateTime
    fun provideMediumDateTimeFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(context)
    }

    @Singleton
    @Provides
    @Named("app")
    fun provideAppPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(context.packageName + "_preferences", Context.MODE_PRIVATE)
    }

}