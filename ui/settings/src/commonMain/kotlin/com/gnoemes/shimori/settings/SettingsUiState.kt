package com.gnoemes.shimori.settings

import androidx.compose.runtime.Immutable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class SettingsUiState(
    val appName: String,
    val versionName: String,
    val navigateUpEnabled : Boolean,
    val eventSink: (SettingsUiEvent) -> Unit
) : CircuitUiState

sealed interface SettingsUiEvent : CircuitUiEvent {
    data object OpenAppearenceSettings : SettingsUiEvent
    data object OpenGithub : SettingsUiEvent
    data object OpenShikimori : SettingsUiEvent
    data object NavigateUp : SettingsUiEvent
}