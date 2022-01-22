package com.gnoemes.shimori.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.extensions.combine
import com.gnoemes.shimori.base.settings.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @Named("app_version") appVersion: String,
    private val settings: ShimoriSettings
) : ViewModel() {

    val state = combine(
        MutableStateFlow(appVersion),
        settings.titlesLocale.observe,
        settings.locale.observe,
        settings.showSpoilers.observe,
        settings.theme.observe,
        settings.accentColor.observe,
    ) { appVersion, titlesLocale, locale, showSpoilers, theme, accentColor ->
        SettingsViewState(appVersion, titlesLocale, locale, showSpoilers, theme, accentColor)
    }

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