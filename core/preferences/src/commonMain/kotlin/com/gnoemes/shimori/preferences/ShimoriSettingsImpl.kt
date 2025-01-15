package com.gnoemes.shimori.preferences

import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.settings.AppAccentColor
import com.gnoemes.shimori.settings.AppLocale
import com.gnoemes.shimori.settings.AppTheme
import com.gnoemes.shimori.settings.AppTitlesLocale
import com.gnoemes.shimori.settings.Setting
import com.gnoemes.shimori.settings.ShimoriSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@OptIn(ExperimentalSettingsApi::class)
@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class ShimoriSettingsImpl(
    private val storage: AppObservableSettings,
    private val applicationInfo: ApplicationInfo,
    dispatchers: AppCoroutineDispatchers,
) : ShimoriSettings {
    private val flowSettings by lazy { storage.toFlowSettings(dispatchers.io) }

    private companion object {
        const val APP_LOCALE = "APP_LOCALE"
        const val TITLES_LOCALE = "TITLES_LOCALE"
        const val SHOW_SPOILERS = "SHOW_SPOILERS"
        const val APP_THEME = "APP_THEME"
        const val ACCENT_COLOR = "ACCENT_COLOR"
    }

    override val titlesLocale = object : Setting<AppTitlesLocale> {
        override suspend fun update(newState: AppTitlesLocale) {
            storage.putInt(TITLES_LOCALE, newState.value)
        }

        override val observe: Flow<AppTitlesLocale>
            get() = flowSettings.getIntOrNullFlow(TITLES_LOCALE).map { locale ->
                if (locale == null) when (AppLocale.from(applicationInfo.defaultLocale)) {
                    AppLocale.Russian -> AppTitlesLocale.Russian
                    else -> AppTitlesLocale.English
                }
                else AppTitlesLocale.from(locale)
            }

    }

    override val locale = object : Setting<AppLocale> {
        override suspend fun update(newState: AppLocale) {
            storage.putInt(APP_LOCALE, newState.value)
        }

        override val observe: Flow<AppLocale>
            get() = flowSettings.getIntOrNullFlow(APP_LOCALE).map { locale ->
                if (locale == null) AppLocale.from(applicationInfo.defaultLocale)
                else AppLocale.from(locale)
            }
    }

    override val showSpoilers = object : Setting<Boolean> {
        override suspend fun update(newState: Boolean) {
            storage.putBoolean(SHOW_SPOILERS, newState)
        }

        override val observe: Flow<Boolean>
            get() = flowSettings.getBooleanFlow(SHOW_SPOILERS, false)

    }

    override val theme = object : Setting<AppTheme> {
        override suspend fun update(newState: AppTheme) {
            storage.putString(APP_THEME, newState.name)
        }

        override val observe: Flow<AppTheme>
            get() = flowSettings.getStringOrNullFlow(APP_THEME).map { theme ->
                theme?.let { AppTheme.valueOf(it) } ?: AppTheme.SYSTEM
            }

    }

    override val accentColor = object : Setting<AppAccentColor> {
        override suspend fun update(newState: AppAccentColor) {
            storage.putInt(ACCENT_COLOR, newState.value)
        }

        override val observe: Flow<AppAccentColor>
            get() = flowSettings.getIntOrNullFlow(ACCENT_COLOR).map { color ->
                //TODO check system color available
                val defaultColor = when (storage.getStringOrNull(APP_THEME)) {
                    AppTheme.LIGHT.name -> AppAccentColor.Orange
                    else -> AppAccentColor.Orange
                }
                color?.let { AppAccentColor.from(it) } ?: defaultColor
            }
    }
}