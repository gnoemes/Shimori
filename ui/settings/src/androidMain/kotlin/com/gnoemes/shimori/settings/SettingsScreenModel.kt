package com.gnoemes.shimori.settings

import androidx.compose.runtime.Immutable
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.base.core.settings.AppAccentColor
import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTheme
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.base.core.settings.invoke
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SettingsScreenModel(
    appVersion: String,
    private val settings: ShimoriSettings,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<SettingsScreenState?>(null) {

    init {
        coroutineScope.launch(dispatchers.io) {
            combine(
                MutableStateFlow(appVersion),
                settings.titlesLocale.observe,
                settings.locale.observe,
                settings.showSpoilers.observe,
                settings.theme.observe,
                settings.accentColor.observe,
                ::SettingsScreenState
            ).collectLatest { state ->
                mutableState.update { state }
            }
        }
    }

    fun onChangeTitlesLocale(newState: AppTitlesLocale) {
        coroutineScope.launch { settings.titlesLocale(newState) }
    }

    fun onChangeLocale(newState: AppLocale) {
        coroutineScope.launch { settings.locale(newState) }
    }

    fun onChangeSpoilers(newState: Boolean) {
        coroutineScope.launch { settings.showSpoilers(newState) }
    }

    fun onChangeTheme(newState: AppTheme) {
        coroutineScope.launch { settings.theme(newState) }
    }

    fun onChangeSecondaryColor(newState: AppAccentColor) {
        coroutineScope.launch { settings.accentColor(newState) }
    }
}

@Immutable
internal data class SettingsScreenState(
    val appVersion: String,
    val titlesLocale: AppTitlesLocale,
    val locale: AppLocale,
    val showSpoilers: Boolean,
    val theme: AppTheme,
    val accentColor: AppAccentColor,
)