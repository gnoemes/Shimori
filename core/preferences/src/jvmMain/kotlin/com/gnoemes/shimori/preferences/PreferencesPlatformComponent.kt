package com.gnoemes.shimori.preferences

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.russhwolf.settings.PreferencesSettings
import me.tatarka.inject.annotations.Provides
import java.util.prefs.Preferences

actual interface PreferencesPlatformComponent {
    @ApplicationScope
    @Provides
    fun providePreferences(delegate: AppPreferences): AppObservablePreferences =
        PreferencesSettings(delegate)

    @ApplicationScope
    @Provides
    fun provideSettings(delegate: AppSettings): AppObservableSettings =
        PreferencesSettings(delegate)

    @ApplicationScope
    @Provides
    fun provideAuthPreferences(delegate: AppAuthPreferences): AppAuthObservablePreferences =
        PreferencesSettings(delegate)

    @ApplicationScope
    @Provides
    fun provideAuthPreferences(): AppAuthPreferences =
        Preferences.userRoot().node("com.gnoemes.shimori.sources.auth")

    @ApplicationScope
    @Provides
    fun providePreferences(): AppPreferences =
        Preferences.userRoot().node("com.gnoemes.shimori.prefs")

    @ApplicationScope
    @Provides
    fun provideSettings(): AppSettings =
        Preferences.userRoot().node("com.gnoemes.shimori.settings")
}

typealias AppPreferences = Preferences
typealias AppAuthPreferences = Preferences
typealias AppSettings = Preferences