package com.gnoemes.shimori.auth

import androidx.compose.runtime.Composable
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.domain.interactors.SignInSource
import com.gnoemes.shimori.screens.AuthScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = AuthScreen::class, UiScope::class)
class AuthPresenter(
    @Assisted private val navigator: Navigator,
    private val signInSource: Lazy<SignInSource>,
) : Presenter<AuthUiState> {

    @Composable
    override fun present(): AuthUiState {
        TODO("Not yet implemented")
    }
}