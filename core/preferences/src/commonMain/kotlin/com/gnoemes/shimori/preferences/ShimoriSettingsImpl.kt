package com.gnoemes.shimori.preferences

import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.entities.isAndroid
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.settings.AppAccentColor
import com.gnoemes.shimori.settings.AppLocale
import com.gnoemes.shimori.settings.AppTheme
import com.gnoemes.shimori.settings.AppTitlesLocale
import com.gnoemes.shimori.settings.OnProgressCompleteAction
import com.gnoemes.shimori.settings.OnProgressNotInWatchingAction
import com.gnoemes.shimori.settings.Setting
import com.gnoemes.shimori.settings.ShimoriSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
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
    private val flow by lazy { storage.toFlowSettings(dispatchers.io) }
    private val settings by lazy { storage.toSuspendSettings(dispatchers.io) }

    private companion object {
        const val APP_LOCALE = "APP_LOCALE"
        const val TITLES_LOCALE = "TITLES_LOCALE"
        const val SHOW_SPOILERS = "SHOW_SPOILERS"
        const val APP_THEME = "APP_THEME"
        const val ACCENT_COLOR = "ACCENT_COLOR"

        const val ON_PROGRESS_COMPLETE_ACTION = "ON_PROGRESS_COMPLETE_ACTION"
        const val ON_PROGRESS_NOT_IN_WATCHING_ACTION = "ON_PROGRESS_NOT_IN_WATCHING_ACTION"
    }

    override val titlesLocale = object : Setting<AppTitlesLocale> {
        private val key = TITLES_LOCALE

        private fun getOrDefault(value: Int?): AppTitlesLocale {
            return if (value == null) when (AppLocale.from(applicationInfo.defaultLocale)) {
                AppLocale.Russian -> AppTitlesLocale.Russian
                else -> AppTitlesLocale.English
            }
            else AppTitlesLocale.from(value)
        }

        override suspend fun update(newState: AppTitlesLocale) {
            storage.putInt(key, newState.value)
        }

        override suspend fun get(): AppTitlesLocale = settings.getIntOrNull(key).let(::getOrDefault)
        override val observe: Flow<AppTitlesLocale> = flow.getIntOrNullFlow(key).map(::getOrDefault)
    }

    override val locale = object : Setting<AppLocale> {
        private val key = APP_LOCALE

        private fun getOrDefault(value: Int?): AppLocale {
            return if (value == null) AppLocale.from(applicationInfo.defaultLocale)
            else AppLocale.from(value)
        }

        override suspend fun update(newState: AppLocale) {
            storage.putInt(key, newState.value)
        }

        override suspend fun get(): AppLocale = settings.getIntOrNull(key).let(::getOrDefault)
        override val observe: Flow<AppLocale> = flow.getIntOrNullFlow(key).map(::getOrDefault)
    }

    override val showSpoilers = object : Setting<Boolean> {
        private val key = SHOW_SPOILERS

        override suspend fun update(newState: Boolean) {
            storage.putBoolean(key, newState)
        }

        override suspend fun get(): Boolean = settings.getBoolean(key, false)
        override val observe: Flow<Boolean> = flow.getBooleanFlow(key, false)

    }

    override val theme = object : Setting<AppTheme> {
        private val key = APP_THEME
        private fun getOrDefault(value: String?): AppTheme {
            return if (value == null) {
                //TODO check system theme available
                if (applicationInfo.platform.isAndroid) AppTheme.SYSTEM
                else AppTheme.DARK
            } else AppTheme.valueOf(value)
        }

        override suspend fun update(newState: AppTheme) {
            storage.putString(key, newState.name)
        }

        override suspend fun get(): AppTheme = settings.getStringOrNull(key).let(::getOrDefault)
        override val observe: Flow<AppTheme> = flow.getStringOrNullFlow(key).map(::getOrDefault)
    }

    override val accentColor = object : Setting<AppAccentColor> {
        private val key = ACCENT_COLOR
        private fun getOrDefault(value: Int?): AppAccentColor {
            //TODO check system color available
            val defaultColor = when (storage.getStringOrNull(APP_THEME)) {
                AppTheme.LIGHT.name -> AppAccentColor.Orange
                else -> AppAccentColor.Orange
            }
            return value?.let { AppAccentColor.from(it) } ?: defaultColor
        }

        override suspend fun update(newState: AppAccentColor) {
            storage.putInt(key, newState.value)
        }

        override suspend fun get(): AppAccentColor = settings.getIntOrNull(key).let(::getOrDefault)
        override val observe: Flow<AppAccentColor> = flow.getIntOrNullFlow(key).map(::getOrDefault)
    }

    override val onProgressCompleteAction = object : Setting<OnProgressCompleteAction> {
        private val key = ON_PROGRESS_COMPLETE_ACTION

        private fun getOrDefault(value: Int?): OnProgressCompleteAction {
            return if (value == null) OnProgressCompleteAction.OpenTrackEdit
            else OnProgressCompleteAction.from(value)
        }

        override suspend fun update(newState: OnProgressCompleteAction) {
            storage.putInt(key, newState.value)
        }

        override suspend fun get(): OnProgressCompleteAction =
            settings.getIntOrNull(key).let(::getOrDefault)

        override val observe: Flow<OnProgressCompleteAction> =
            flow.getIntOrNullFlow(key).map(::getOrDefault)
    }

    override val onProgressNotInWatchingAction = object : Setting<OnProgressNotInWatchingAction> {
        private val key = ON_PROGRESS_NOT_IN_WATCHING_ACTION

        private fun getOrDefault(value: Int?): OnProgressNotInWatchingAction {
            return if (value == null) OnProgressNotInWatchingAction.MoveToWatching
            else OnProgressNotInWatchingAction.from(value)
        }

        override suspend fun update(newState: OnProgressNotInWatchingAction) {
            storage.putInt(key, newState.value)
        }

        override suspend fun get(): OnProgressNotInWatchingAction =
            settings.getIntOrNull(key).let(::getOrDefault)

        override val observe: Flow<OnProgressNotInWatchingAction> =
            flow.getIntOrNullFlow(key).map(::getOrDefault)
    }
}