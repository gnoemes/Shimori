package com.gnoemes.shimori.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.base.core.settings.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    appVersion: String,
    private val settings: ShimoriSettings
) : ViewModel() {

    val state = combine(
        MutableStateFlow(appVersion),
        settings.titlesLocale.observe,
        settings.locale.observe,
        settings.showSpoilers.observe,
        settings.theme.observe,
        settings.accentColor.observe,
        ::SettingsViewState
    )

    fun onChangeTitlesLocale(newState: AppTitlesLocale) {
        viewModelScope.launch { settings.titlesLocale(newState) }
    }

    fun onChangeLocale(newState: AppLocale) {
        viewModelScope.launch { settings.locale(newState) }
    }

    fun onChangeSpoilers(newState: Boolean) {
        viewModelScope.launch { settings.showSpoilers(newState) }
    }

    fun onChangeTheme(newState: AppTheme) {
        viewModelScope.launch { settings.theme(newState) }
    }

    fun onChangeSecondaryColor(newState: AppAccentColor) {
        viewModelScope.launch { settings.accentColor(newState) }
    }

}