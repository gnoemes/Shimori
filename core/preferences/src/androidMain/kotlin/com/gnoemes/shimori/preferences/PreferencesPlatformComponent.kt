package com.gnoemes.shimori.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.gnoemes.shimori.base.inject.ApplicationScope
import com.russhwolf.settings.SharedPreferencesSettings
import me.tatarka.inject.annotations.Provides

actual interface PreferencesPlatformComponent {

    @ApplicationScope
    @Provides
    fun provideObservablePreferences(delegate: AppSharedPreferences): AppObservablePreferences {
        return SharedPreferencesSettings(delegate)
    }

    @ApplicationScope
    @Provides
    fun provideObservableSettings(delegate: AppSettings): AppObservableSettings {
        return SharedPreferencesSettings(delegate)
    }

    @ApplicationScope
    @Provides
    fun provideAuthPreferences(delegate: AppAuthSharedPreferences): AppAuthObservablePreferences =
        SharedPreferencesSettings(delegate)

    @ApplicationScope
    @Provides
    fun provideAppSharedPreferences(
        context: Application
    ): AppSharedPreferences = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)

    @ApplicationScope
    @Provides
    fun provideAuthPreferences(
        context: Application
    ): AppAuthSharedPreferences = context.getSharedPreferences("shimori_source_auth", Context.MODE_PRIVATE)


    @ApplicationScope
    @Provides
    fun provideAppSettings(
        context: Application
    ): AppSettings = context.getSharedPreferences("shimori_settings", Context.MODE_PRIVATE)
}

typealias AppSharedPreferences = SharedPreferences
typealias AppAuthSharedPreferences = SharedPreferences
typealias AppSettings = SharedPreferences