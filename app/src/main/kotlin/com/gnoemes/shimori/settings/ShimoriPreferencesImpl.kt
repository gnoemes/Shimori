package com.gnoemes.shimori.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.gnoemes.shimori.R
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.base.settings.ShimoriPreferences.Theme
import com.gnoemes.shimori.model.rate.ListType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named

class ShimoriPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Named("app") private val prefs: SharedPreferences
) : ShimoriPreferences {
    private val defaultThemeValue = context.getString(R.string.pref_theme_system_value)
    private val preferenceKeyChangedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    companion object {
        private const val THEME_KEY = "pref_theme"
        private const val IS_ROMADZI_NAMING = "IS_ROMADZI_NAMING"
        private const val PREFERRED_LIST_TYPE = "PREFERRED_LIST_TYPE"
    }

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        preferenceKeyChangedFlow.tryEmit(key)
    }

    override fun setup() {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override var theme: Theme
        get() = getThemeForStorageValue(prefs.getString(THEME_KEY, defaultThemeValue)!!)
        set(value) = prefs.edit {
            putString(THEME_KEY, getStorageKeyForTheme(value))
        }

    override fun observeTheme(): Flow<Theme> {
        return preferenceKeyChangedFlow
            // Emit on start so that we always send the initial value
            .onStart { emit(THEME_KEY) }
            .filter { it == THEME_KEY }
            .map { theme }
            .distinctUntilChanged()
    }

    override var isRomadziNaming: Boolean
        get() = prefs.getBoolean(IS_ROMADZI_NAMING, false)
        set(value) = prefs.edit { putBoolean(IS_ROMADZI_NAMING, value) }

    override var preferredListType: Int
        get() = prefs.getInt(PREFERRED_LIST_TYPE, ListType.Anime.type)
        set(value) = prefs.edit { putInt(PREFERRED_LIST_TYPE, value) }

    private fun getStorageKeyForTheme(theme: Theme) = when (theme) {
        Theme.LIGHT -> context.getString(R.string.pref_theme_light_value)
        Theme.DARK -> context.getString(R.string.pref_theme_dark_value)
        Theme.SYSTEM -> context.getString(R.string.pref_theme_system_value)
    }

    private fun getThemeForStorageValue(value: String) = when (value) {
        context.getString(R.string.pref_theme_light_value) -> Theme.LIGHT
        context.getString(R.string.pref_theme_dark_value) -> Theme.DARK
        else -> Theme.SYSTEM
    }
}