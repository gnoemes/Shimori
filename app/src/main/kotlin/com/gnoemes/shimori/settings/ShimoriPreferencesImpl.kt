package com.gnoemes.shimori.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.gnoemes.shimori.R
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.base.settings.ShimoriPreferences.Theme
import javax.inject.Inject
import javax.inject.Named

class ShimoriPreferencesImpl @Inject constructor(
    private val context: Context,
    @Named("app") private val prefs: SharedPreferences
) : ShimoriPreferences {
    private val defaultThemeValue = context.getString(R.string.pref_theme_default_value)

    companion object {
        private const val THEME_KEY = "pref_theme"
        const val IS_ROMADZI_NAMING = "IS_ROMADZI_NAMING"
    }

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            THEME_KEY -> updateUsingThemePreference()
        }
    }

    override fun setup() {
        updateUsingThemePreference()
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override var themePreference: Theme
        get() = getThemeForStorageValue(prefs.getString(THEME_KEY, defaultThemeValue)!!)
        set(value) = prefs.edit {
            putString(THEME_KEY, getStorageKeyForTheme(value))
        }

    override var isRussianNaming: Boolean
        get() = prefs.getBoolean(IS_ROMADZI_NAMING, true)
        set(value) = prefs.edit { putBoolean(IS_ROMADZI_NAMING, value) }

    private fun updateUsingThemePreference() = when (themePreference) {
        Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Theme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        Theme.BATTERY_SAVER_ONLY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
    }

    private fun getStorageKeyForTheme(theme: Theme) = when (theme) {
        Theme.LIGHT -> context.getString(R.string.pref_theme_light_value)
        Theme.DARK -> context.getString(R.string.pref_theme_dark_value)
        Theme.BATTERY_SAVER_ONLY -> context.getString(R.string.pref_theme_battery_value)
        Theme.SYSTEM -> context.getString(R.string.pref_theme_system_value)
    }

    private fun getThemeForStorageValue(value: String) = when (value) {
        context.getString(R.string.pref_theme_system_value) -> Theme.SYSTEM
        context.getString(R.string.pref_theme_light_value) -> Theme.LIGHT
        context.getString(R.string.pref_theme_dark_value) -> Theme.DARK
        context.getString(R.string.pref_theme_battery_value) -> Theme.BATTERY_SAVER_ONLY
        else -> throw IllegalArgumentException("Invalid preference value for theme")
    }
}