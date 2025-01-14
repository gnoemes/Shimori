package com.gnoemes.shimori.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.russhwolf.settings.SharedPreferencesSettings
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual interface PreferencesPlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideObservablePreferences(delegate: AppSharedPreferences): AppObservablePreferences {
        return SharedPreferencesSettings(delegate)
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideObservableSettings(delegate: AppSettings): AppObservableSettings {
        return SharedPreferencesSettings(delegate)
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideAuthPreferences(delegate: AppAuthSharedPreferences): AppAuthObservablePreferences =
        SharedPreferencesSettings(delegate)

    @SingleIn(AppScope::class)
    @Provides
    fun provideAppSharedPreferences(
        context: Application
    ): AppSharedPreferences = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)

    @SingleIn(AppScope::class)
    @Provides
    fun provideAuthPreferences(
        context: Application
    ): AppAuthSharedPreferences =
        context.getSharedPreferences("shimori_source_auth", Context.MODE_PRIVATE)


    @SingleIn(AppScope::class)
    @Provides
    fun provideAppSettings(
        context: Application
    ): AppSettings = context.getSharedPreferences("shimori_settings", Context.MODE_PRIVATE)
}

typealias AppSharedPreferences = SharedPreferences
typealias AppAuthSharedPreferences = SharedPreferences
typealias AppSettings = SharedPreferences