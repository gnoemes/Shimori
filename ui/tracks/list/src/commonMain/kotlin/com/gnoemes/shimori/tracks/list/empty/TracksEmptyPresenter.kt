package com.gnoemes.shimori.tracks.list.empty

import androidx.compose.runtime.Composable
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.screens.ExploreScreen
import com.gnoemes.shimori.screens.TracksEmptyScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TracksEmptyScreen::class, UiScope::class)
class TracksEmptyPresenter(
    @Assisted private val navigator: Navigator,
) : Presenter<TrackEmptyUiState> {

    @Composable
    override fun present(): TrackEmptyUiState {

        val eventSink: CoroutineScope.(TracksEmptyUiEvent) -> Unit = { event ->
            when (event) {
                is TracksEmptyUiEvent.OpenExplore -> navigator.goTo(ExploreScreen(type = event.type))
            }
        }

        return TrackEmptyUiState(
            availableTypes = TrackTargetType.entries,
            eventSink = wrapEventSink(eventSink)
        )
    }
}