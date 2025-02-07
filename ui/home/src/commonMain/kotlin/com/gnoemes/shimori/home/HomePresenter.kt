package com.gnoemes.shimori.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.ui.overlay.showInSideSheet
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.lists.ListsStateBus
import com.gnoemes.shimori.data.lists.ListsUiEvents
import com.gnoemes.shimori.data.source.auth.AuthManager
import com.gnoemes.shimori.domain.interactors.LogoutSource
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.screens.HomeScreen
import com.gnoemes.shimori.screens.TrackEditScreen
import com.gnoemes.shimori.sources.SourceIds
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.overlay.LocalOverlayHost
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = HomeScreen::class, UiScope::class)
class HomePresenter(
    @Assisted private val navigator: Navigator,
    private val observeShikimoriAuth: Lazy<ObserveShikimoriAuth>,
    private val observeMyUserShort: Lazy<ObserveMyUserShort>,
    private val authManager: Lazy<AuthManager>,
    private val logoutSource: Lazy<LogoutSource>,
    private val stateBus: ListsStateBus,
) : Presenter<HomeUiState> {

    private val isAuthorizedBefore by lazy { authManager.value.isAuthorized(SourceIds.SHIKIMORI) }

    @Composable
    override fun present(): HomeUiState {
        val isAuthorized by observeShikimoriAuth.value.flow.map { it.isAuthorized }
            .distinctUntilChanged()
            .collectAsRetainedState(isAuthorizedBefore)
        val profileImage by observeMyUserShort.value.flow.map { it?.image }
            .collectAsRetainedState(null)

        val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
        val isCompact by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isCompact() } }
        val overlayHost = LocalOverlayHost.current

        LaunchedEffect(Unit) {
            observeShikimoriAuth.value(Unit)
            observeMyUserShort.value(Unit)

            stateBus.uiEvents.observe.collect { event ->
                when (event) {
                    is ListsUiEvents.OpenEdit -> {
                        val screen = TrackEditScreen(
                            event.targetId,
                            event.targetType,
                            event.predefinedStatus
                        )
                        if (isCompact) navigator.goTo(screen)
                        else overlayHost.showInSideSheet(screen)
                    }

                    else -> Unit
                }
            }
        }

        val eventSink: CoroutineScope.(HomeUiEvent) -> Unit = { event ->
            when (event) {
                is HomeUiEvent.OnNavEvent -> navigator.onNavEvent(event.navEvent)
                is HomeUiEvent.Logout -> launchOrThrow {
                    logoutSource.value(LogoutSource.Params(SourceIds.SHIKIMORI))
                }

                else -> {}
//                HomeUiEvent.OpenSearch -> navigator
            }
        }

        return HomeUiState(
            isAuthorized = isAuthorized,
            profileImage = profileImage,
            eventSink = wrapEventSink(eventSink)
        )
    }

}