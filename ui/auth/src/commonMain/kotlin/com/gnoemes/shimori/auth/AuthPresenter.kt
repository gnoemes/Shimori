package com.gnoemes.shimori.auth

import androidx.compose.runtime.Composable
import com.gnoemes.shimori.screens.AuthScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class AuthUiPresenterFactory(
    private val presenterFactory: (Navigator) -> AuthPresenter,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext
    ): Presenter<*>? = when (screen) {
        is AuthScreen -> presenterFactory(navigator)
        else -> null
    }
}


@Inject
class AuthPresenter(
    @Assisted val navigator: Navigator,
) : Presenter<AuthUiState> {

    @Composable
    override fun present(): AuthUiState {


        fun eventSink(event: AuthUiEvent) {

        }

        return AuthUiState(
            null,
            eventSink = ::eventSink
        )
    }
}