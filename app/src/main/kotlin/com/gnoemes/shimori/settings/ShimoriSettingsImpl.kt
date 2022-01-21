package com.gnoemes.shimori.settings

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.gnoemes.shimori.base.settings.Setting
import com.gnoemes.shimori.base.settings.ShimoriSettings
import com.gnoemes.shimori.common.compose.theme.PaletteDark
import com.gnoemes.shimori.common.compose.theme.PaletteLight
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

        private val APP_THEME = stringPreferencesKey("app_theme")
        private val ROMADZI_NAMING = booleanPreferencesKey("romadzi_naming")
        private val DYNAMIC_COLORS = booleanPreferencesKey("dynamic_colors")
        private val SECONDARY_COLOR = stringPreferencesKey("secondary_color")
        private val LOCALE = stringPreferencesKey("locale")
        private val PREFERRED_LIST = intPreferencesKey("list")
        private val PREFERRED_STATUS = stringPreferencesKey("status")
    }

    override val theme = object : Setting<ShimoriSettings.Theme> {
        override suspend fun update(newState: ShimoriSettings.Theme) {
            context.store.edit { prefs ->
                prefs[APP_THEME] = newState.name
            }
        }

        override val observe: Flow<ShimoriSettings.Theme>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    ShimoriSettings.Theme.valueOf(
                        preferences[APP_THEME] ?: ShimoriSettings.Theme.SYSTEM.name
                    )
                }
    }

    override val dynamicColors = object : Setting<Boolean> {
        override suspend fun update(newState: Boolean) {
            context.store.edit { prefs ->
                prefs[DYNAMIC_COLORS] = newState
            }
        }

        override val observe: Flow<Boolean>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    preferences[DYNAMIC_COLORS] ?: true
                }
    }

    override val secondaryColor = object : Setting<String> {
        override suspend fun update(newState: String) {
            context.store.edit { prefs ->
                prefs[SECONDARY_COLOR] = newState
            }
        }

        override val observe: Flow<String>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    val defaultColor = when (preferences[APP_THEME]) {
                        ShimoriSettings.Theme.LIGHT.name -> PaletteLight.primary.value.toString()
                        else -> PaletteDark.primary.value.toString()
                    }
                    preferences[SECONDARY_COLOR] ?: defaultColor
                }
    }

    override val locale = object : Setting<String> {
        override suspend fun update(newState: String) {
            context.store.edit { prefs ->
                prefs[LOCALE] = newState
            }
        }

        override val observe: Flow<String>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    val defaultLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        context.resources.configuration.locales[0].language
                    } else {
                        context.resources.configuration.locale.language
                    }
                    preferences[LOCALE] ?: defaultLocale
                }
    }

    override val isRomadziNaming = object : Setting<Boolean> {
        override suspend fun update(newState: Boolean) {
            context.store.edit { prefs ->
                prefs[ROMADZI_NAMING] = newState
            }
        }

        override val observe: Flow<Boolean>
            get() = context.store.data
                .catchIO()
                .map { preferences ->
                    preferences[ROMADZI_NAMING] ?: false
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