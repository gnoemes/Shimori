package com.gnoemes.shimori.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.domain.interactors.SignInSource
import com.gnoemes.shimori.domain.observers.ObserveAuthSources
import com.gnoemes.shimori.screens.AuthScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = AuthScreen::class, UiScope::class)
class AuthPresenter(
    @Assisted private val navigator: Navigator,
    private val observeAuthSources: Lazy<ObserveAuthSources>,
    private val signInSource: Lazy<SignInSource>,
) : Presenter<AuthUiState> {

    @Composable
    override fun present(): AuthUiState {

        val sources by observeAuthSources.value.flow.collectAsRetainedState(emptyList())

        LaunchedEffect(Unit) {
            observeAuthSources.value(Unit)
        }

        val eventSink: CoroutineScope.(AuthUiEvent) -> Unit = { event ->
            when (event) {
                is AuthUiEvent.SignIn -> launchOrThrow {
                    signInSource.value(SignInSource.Params(event.sourceId))
                }
            }
        }

        return AuthUiState(
            availableSources = sources,
            eventSink = wrapEventSink(eventSink)
        )
    }

}