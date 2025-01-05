package com.gnoemes.shimori.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.screens.AuthScreen
import com.slack.circuit.codegen.annotations.CircuitInject


@Composable
@CircuitInject(screen = AuthScreen::class, UiScope::class)
internal fun AuthUi(
    state: AuthUiState,
    modifier: Modifier = Modifier
) {

    val eventSink = state.eventSink
    AuthUi(
        state = state,

    )
}

@Composable
private fun AuthUi(
    state: AuthUiState,
    signIn: (SourceId: Long) -> Unit,
) {

}