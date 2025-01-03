package com.gnoemes.shimori.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gnoemes.shimori.screens.AuthScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import me.tatarka.inject.annotations.Inject

@Inject
class AuthUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is AuthScreen -> {
            ui<AuthUiState> { state, modifier -> Auth(state, modifier) }
        }
        else -> null
    }
}

@Composable
internal fun Auth(
    state: AuthUiState,
    modifier: Modifier
) {

}