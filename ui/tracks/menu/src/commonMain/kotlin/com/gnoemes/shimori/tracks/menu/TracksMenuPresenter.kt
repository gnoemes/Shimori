package com.gnoemes.shimori.tracks.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.lists.ListsStateBus
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.observers.ObserveExistedStatuses
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.screens.TracksMenuScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TracksMenuScreen::class, UiScope::class)
class TracksMenuPresenter(
    @Assisted private val navigator: Navigator,
    private val listsState: ListsStateBus,
    private val prefs: ShimoriPreferences,
    private val observeAnimeStatuses: Lazy<ObserveExistedStatuses>,
    private val observeMangaStatuses: Lazy<ObserveExistedStatuses>,
    private val observeRanobeStatuses: Lazy<ObserveExistedStatuses>,
) : Presenter<TracksMenuUiState> {
    @Composable
    override fun present(): TracksMenuUiState {
        val type by listsState.type.observe.collectAsRetainedState(TrackTargetType.valueOf(prefs.preferredListType))
        val status by listsState.page.observe.collectAsRetainedState(TrackStatus.valueOf(prefs.preferredListStatus))

        val animeStatuses by observeAnimeStatuses.value.flow.collectAsRetainedState(emptyList())
        val mangaStatuses by observeMangaStatuses.value.flow.collectAsRetainedState(emptyList())
        val ranobeStatuses by observeRanobeStatuses.value.flow.collectAsRetainedState(emptyList())

        val availableStatuses by remember {
            derivedStateOf {
                mapOf(
                    TrackTargetType.ANIME to animeStatuses,
                    //show manga & ranobe in single list
                    TrackTargetType.MANGA to (mangaStatuses.toSet() + ranobeStatuses.toSet()).toList()
                )
            }
        }

        LaunchedEffect(Unit) {
            observeAnimeStatuses.value(ObserveExistedStatuses.Params(TrackTargetType.ANIME))
            observeMangaStatuses.value(ObserveExistedStatuses.Params(TrackTargetType.MANGA))
            observeRanobeStatuses.value(ObserveExistedStatuses.Params(TrackTargetType.RANOBE))
        }

        val eventSink: CoroutineScope.(TracksMenuUiEvent) -> Unit = { event ->
            when (event) {
                is TracksMenuUiEvent.NavigateUp -> navigator.pop()
                is TracksMenuUiEvent.OpenStatus -> launchOrThrow {
                    listsState.type.update(event.type)
                    listsState.page.update(event.status)

                    prefs.preferredListType = event.type.name
                    prefs.preferredListStatus = event.status.name
                    navigator.pop()
                }
            }
        }

        return TracksMenuUiState(
            selectedType = type,
            selectedStatus = status,
            availableStatuses = availableStatuses,
            eventSink = wrapEventSink(eventSink)
        )
    }
}