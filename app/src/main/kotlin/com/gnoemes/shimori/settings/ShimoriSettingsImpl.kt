package com.gnoemes.shimori.settings

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.gnoemes.shimori.base.settings.*
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class ShimoriSettingsImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ShimoriSettings {

    companion object {
        private val Context.store: DataStore<Preferences> by preferencesDataStore("shimori_settings")

        private val TITlES_LOCALE = intPreferencesKey("titles_locale")
        private val LOCALE = intPreferencesKey("locale")
        private val SPOILERS = booleanPreferencesKey("spoilers")
        private val APP_THEME = stringPreferencesKey("app_theme")
        private val ACCENT_COLOR = intPreferencesKey("accent_color")

        private val PREFERRED_LIST = intPreferencesKey("list")
        private val PREFERRED_STATUS = stringPreferencesKey("status")
    }

    override val titlesLocale = object : Setting<AppTitlesLocale> {
        override suspend fun update(newState: AppTitlesLocale) {
            context.store.edit { prefs ->
                prefs[TITlES_LOCALE] = newState.value
            }
        }

        override val observe: Flow<AppTitlesLocale>
            get() = context.store.data
                .catchIO()
                .map { preferences ->

                    val default = when (preferences[LOCALE]?.let { AppLocale.from(it) }) {
                        AppLocale.Russian -> AppTitlesLocale.Russian
                        else -> AppTitlesLocale.English
                    }

                    preferences[TITlES_LOCALE]?.let {
                        AppTitlesLocale.from(it)
                    } ?: default
                }
    }

    override val locale = object : Setting<AppLocale> {
        override suspend fun update(newState: AppLocale) {
            context.store.edit { prefs ->
                prefs[LOCALE] = newState.value
            }
        }

        override val observe: Flow<AppLocale>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    val language = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        context.resources.configuration.locales[0].language
                    } else {
                        context.resources.configuration.locale.language
                    }

                    preferences[LOCALE]?.let { AppLocale.from(it) } ?: AppLocale.from(language)
                }
    }

    override val showSpoilers = object : Setting<Boolean> {
        override suspend fun update(newState: Boolean) {
            context.store.edit { prefs ->
                prefs[SPOILERS] = newState
            }
        }

        override val observe: Flow<Boolean>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    preferences[SPOILERS] ?: false
                }
    }

    override val theme = object : Setting<AppTheme> {
        override suspend fun update(newState: AppTheme) {
            context.store.edit { prefs ->
                prefs[APP_THEME] = newState.name
            }
        }

        override val observe: Flow<AppTheme>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    AppTheme.valueOf(
                        preferences[APP_THEME] ?: AppTheme.SYSTEM.name
                    )
                }
    }

    override val accentColor = object : Setting<AppAccentColor> {
        override suspend fun update(newState: AppAccentColor) {
            context.store.edit { prefs ->
                prefs[ACCENT_COLOR] = newState.value
            }
        }

        override val observe: Flow<AppAccentColor>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    val defaultColor = when (preferences[APP_THEME]) {
                        AppTheme.LIGHT.name -> AppAccentColor.Orange
                        else -> AppAccentColor.Yellow
                    }
                    preferences[ACCENT_COLOR]?.let { AppAccentColor.from(it) } ?: defaultColor
                }
    }

    override val preferredListType = object : Setting<Int> {
        override suspend fun update(newState: Int) {
            context.store.edit { prefs ->
                prefs[PREFERRED_LIST] = newState
            }
        }

        override val observe: Flow<Int>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    preferences[PREFERRED_LIST] ?: ListType.Anime.type
                }
    }

    override val preferredListStatus = object : Setting<String> {
        override suspend fun update(newState: String) {
            context.store.edit { prefs ->
                prefs[PREFERRED_STATUS] = newState
            }
        }

        override val observe: Flow<String>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    //anime is default
                    preferences[PREFERRED_STATUS]
                        ?: RateStatus.listPagesOrder.first().shikimoriValue
                }
    }

    private fun Flow<Preferences>.catchIO(): Flow<Preferences> {
        return catch { e ->
            if (e is IOException) emit(emptyPreferences())
            else throw e
        }
    }
}