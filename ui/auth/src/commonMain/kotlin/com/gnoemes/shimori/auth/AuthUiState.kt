package com.gnoemes.shimori.auth

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.common.compose.ui.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class AuthUiState(
    val message: UiMessage? = null,
    val eventSink: (AuthUiEvent) -> Unit
) : CircuitUiState

sealed interface AuthUiEvent : CircuitUiEvent {
    data object ConnectShikimori : AuthUiEvent
}