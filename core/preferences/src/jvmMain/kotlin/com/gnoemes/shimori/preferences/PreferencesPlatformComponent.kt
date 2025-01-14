package com.gnoemes.shimori.preferences

import com.russhwolf.settings.PreferencesSettings
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.util.prefs.Preferences

actual interface PreferencesPlatformComponent {
    @SingleIn(AppScope::class)
    @Provides
    fun providePreferences(delegate: AppPreferences): AppObservablePreferences =
        PreferencesSettings(delegate)

    @SingleIn(AppScope::class)
    @Provides
    fun provideSettings(delegate: AppSettings): AppObservableSettings =
        PreferencesSettings(delegate)

    @SingleIn(AppScope::class)
    @Provides
    fun provideAuthPreferences(delegate: AppAuthPreferences): AppAuthObservablePreferences =
        PreferencesSettings(delegate)

    @SingleIn(AppScope::class)
    @Provides
    fun provideAuthPreferences(): AppAuthPreferences =
        Preferences.userRoot().node("com.gnoemes.shimori.sources.auth")

    @SingleIn(AppScope::class)
    @Provides
    fun providePreferences(): AppPreferences =
        Preferences.userRoot().node("com.gnoemes.shimori.prefs")

    @SingleIn(AppScope::class)
    @Provides
    fun provideSettings(): AppSettings =
        Preferences.userRoot().node("com.gnoemes.shimori.settings")
}

typealias AppPreferences = Preferences
typealias AppAuthPreferences = Preferences
typealias AppSettings = Preferences