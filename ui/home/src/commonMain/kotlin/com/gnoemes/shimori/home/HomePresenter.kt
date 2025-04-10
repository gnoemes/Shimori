package com.gnoemes.shimori.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.compose.ui.UiMessage
import com.gnoemes.shimori.common.compose.ui.UiMessageManager
import com.gnoemes.shimori.common.ui.overlay.showInSideSheet
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.eventbus.EventBus
import com.gnoemes.shimori.data.events.TrackUiEvents
import com.gnoemes.shimori.data.source.auth.AuthManager
import com.gnoemes.shimori.domain.interactors.LogoutSource
import com.gnoemes.shimori.domain.interactors.tracks.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.preferences.ShimoriPreferences
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
    private val prefs: ShimoriPreferences,
    private val observeShikimoriAuth: Lazy<ObserveShikimoriAuth>,
    private val observeMyUserShort: Lazy<ObserveMyUserShort>,
    private val authManager: Lazy<AuthManager>,
    private val logoutSource: Lazy<LogoutSource>,
    private val createOrUpdateTrack: Lazy<CreateOrUpdateTrack>,
) : Presenter<HomeUiState> {

    private val isAuthorizedBefore by lazy { authManager.value.isAuthorized(SourceIds.SHIKIMORI) }

    @Composable
    override fun present(): HomeUiState {
        val isAuthorized by observeShikimoriAuth.value.flow.map { it.isAuthorized }
            .distinctUntilChanged()
            .collectAsRetainedState(isAuthorizedBefore)
        val profileImage by observeMyUserShort.value.flow.map { it?.image }
            .collectAsRetainedState(null)

        val uiMessageManager = remember { UiMessageManager() }

        val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
        val isCompact by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isCompact() } }
        val overlayHost = LocalOverlayHost.current

        val message by uiMessageManager.message.collectAsState(null)
        val fabSpacing by prefs.observeNestedScaffoldContainsFab().collectAsState(false)

        LaunchedEffect(Unit) {
            observeShikimoriAuth.value(Unit)
            observeMyUserShort.value(Unit)
        }

        val eventSink: CoroutineScope.(HomeUiEvent) -> Unit = { event ->
            when (event) {
                is HomeUiEvent.OnNavEvent -> navigator.onNavEvent(event.navEvent)
                is HomeUiEvent.Logout -> launchOrThrow {
                    logoutSource.value(LogoutSource.Params(SourceIds.SHIKIMORI))
                }

                is HomeUiEvent.OpenTrackEdit -> launchOrThrow {
                    val screen = TrackEditScreen(
                        event.targetId,
                        event.targetType,
                        event.predefinedStatus
                    )
                    if (isCompact) navigator.goTo(screen)
                    else overlayHost.showInSideSheet(screen)
                }

                is HomeUiEvent.ClearMessage -> launchOrThrow {
                    uiMessageManager.clearMessage(event.id)
                }

                is HomeUiEvent.ActionMessage -> launchOrThrow {
                    //on snackbar action
                    when (val payload = event.message.payload) {
                        //if was deleted
                        is TrackUiEvents.TrackDeleted -> {
                            //restore track
                            createOrUpdateTrack.value(CreateOrUpdateTrack.Params(payload.track))
                        }
                    }
                }

                else -> {}
//                HomeUiEvent.OpenSearch -> navigator
            }
        }

        trackUiEventHandler(uiMessageManager, eventSink)

        return HomeUiState(
            fabSpacing = fabSpacing,
            message = message,
            isAuthorized = isAuthorized,
            profileImage = profileImage,
            eventSink = wrapEventSink(eventSink)
        )
    }

    @Composable
    private fun trackUiEventHandler(
        uiMessageManager: UiMessageManager,
        eventSink: CoroutineScope.(HomeUiEvent) -> Unit,
    ) {
        val textCreator = LocalShimoriTextCreator.current
        var trackUiEvent by remember { mutableStateOf<TrackUiEvents?>(null) }

        val eventPreparedMessage = trackUiEvent?.let { event ->
            UiMessage(
                id = event.eventId,
                message = textCreator { event.message() },
                actionLabel = textCreator.nullable { event.actionLabel() },
                image = when (event) {
                    is TrackUiEvents.TrackDeleted -> event.image
                    else -> null
                },
                payload = event
            )
        }

        LaunchedEffect(Unit) {
            EventBus.observe<TrackUiEvents> {
                if (!it.navigation) trackUiEvent = it
                else when (it) {
                    is TrackUiEvents.OpenEdit -> eventSink(
                        HomeUiEvent.OpenTrackEdit(
                            it.targetId,
                            it.targetType,
                            it.predefinedStatus
                        )
                    )

                    else -> Unit
                }
            }
        }

        eventPreparedMessage?.let {
            LaunchedEffect(it) {
                uiMessageManager.emitMessage(it)
            }
        }
    }

}