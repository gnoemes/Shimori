package com.gnoemes.shimori.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.ui.overlay.wrapEventSink
import com.gnoemes.shimori.data.source.auth.AuthManager
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.screens.HomeScreen
import com.gnoemes.shimori.sources.SourceIds
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = HomeScreen::class, UiScope::class)
class HomePresenter(
    @Assisted private val navigator: Navigator,
    private val observeShikimoriAuth: Lazy<ObserveShikimoriAuth>,
    private val observeMyUserShort: Lazy<ObserveMyUserShort>,
    private val authManager: Lazy<AuthManager>
) : Presenter<HomeUiState> {

    @Composable
    override fun present(): HomeUiState {
        val isAuthorized by observeShikimoriAuth.value.flow.map { it.isAuthorized }
            .collectAsRetainedState(
                authManager.value.isAuthorized(SourceIds.SHIKIMORI)
            )
        val profileImage by observeMyUserShort.value.flow.map { it?.image }
            .collectAsRetainedState(null)

        LaunchedEffect(Unit) {
            observeShikimoriAuth.value(Unit)
            observeMyUserShort.value(Unit)
        }

        val eventSink: CoroutineScope.(HomeUiEvent) -> Unit = { event ->
            when (event) {
                is HomeUiEvent.OnNavEvent -> navigator.onNavEvent(event.navEvent)
            }
        }

        return HomeUiState(
            isAuthorized = isAuthorized,
            profileImage = profileImage,
            eventSink = wrapEventSink(eventSink)
        )
    }

}