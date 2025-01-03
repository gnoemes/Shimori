package com.gnoemes.shimori.preferences

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.settings.ShimoriSettings
import com.russhwolf.settings.ObservableSettings
import me.tatarka.inject.annotations.Provides

expect interface PreferencesPlatformComponent

interface PreferencesComponent : PreferencesPlatformComponent {
    val preferences: ShimoriPreferences
    val settings: ShimoriSettings

    @ApplicationScope
    @Provides
    fun providePreferences(bind: ShimoriPreferencesImpl): ShimoriPreferences = bind

    @ApplicationScope
    @Provides
    fun provideSettings(bind: ShimoriSettingsImpl): ShimoriSettings = bind
}

typealias AppObservablePreferences = ObservableSettings
typealias AppAuthObservablePreferences = ObservableSettings
typealias AppObservableSettings = ObservableSettings