package com.gnoemes.shimori.settings.appearance

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.settings.AppLocale
import com.gnoemes.shimori.settings.AppTheme
import com.gnoemes.shimori.settings.AppTitlesLocale
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class SettingsAppearanceUiState(
    val screenAsCard: Boolean,
    val locale: AppLocale,
    val availableLocales : List<AppLocale>,
    val titlesLocale: AppTitlesLocale,
    val availableTitlesLocale : List<AppTitlesLocale>,
    val theme: AppTheme,
    val availableThemes : List<AppTheme>,
    val eventSink: (SettingsAppearanceUiEvent) -> Unit
) : CircuitUiState

sealed interface SettingsAppearanceUiEvent : CircuitUiEvent {
    data object NavigateUp : SettingsAppearanceUiEvent
    data class ChangeLocale(val new: AppLocale) : SettingsAppearanceUiEvent
    data class ChangeTitlesLocale(val new: AppTitlesLocale) : SettingsAppearanceUiEvent
    data class ChangeTheme(val new: AppTheme) : SettingsAppearanceUiEvent
}