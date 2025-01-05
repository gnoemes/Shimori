package com.gnoemes.shimori.preferences

import com.gnoemes.shimori.settings.ShimoriSettings
import com.russhwolf.settings.ObservableSettings
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

expect interface PreferencesPlatformComponent

@ContributesTo(AppScope::class)
interface PreferencesComponent : PreferencesPlatformComponent {
    val preferences: ShimoriPreferences
    val settings: ShimoriSettings
}

typealias AppObservablePreferences = ObservableSettings
typealias AppAuthObservablePreferences = ObservableSettings
typealias AppObservableSettings = ObservableSettings