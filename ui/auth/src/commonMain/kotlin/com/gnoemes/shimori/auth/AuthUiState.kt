package com.gnoemes.shimori.auth

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.source.Source
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class AuthUiState(
    val availableSources: List<Source>,
    val eventSink: (AuthUiEvent) -> Unit
) : CircuitUiState

sealed interface AuthUiEvent : CircuitUiEvent {
    data class SignIn(val sourceId: Long) : AuthUiEvent
}