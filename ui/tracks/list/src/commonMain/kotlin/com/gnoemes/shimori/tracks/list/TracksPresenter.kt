package com.gnoemes.shimori.tracks.list

import androidx.compose.runtime.Composable
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.screens.SettingsScreen
import com.gnoemes.shimori.screens.TracksScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TracksScreen::class, UiScope::class)
class TracksPresenter(
    @Assisted private val navigator: Navigator,
) : Presenter<TracksUiState> {

    @Composable
    override fun present(): TracksUiState {


        val eventSink: CoroutineScope.(TracksUiEvent) -> Unit = { event ->
            when (event) {
                is TracksUiEvent.OpenSettings -> navigator.goTo(SettingsScreen)
            }
        }

        return TracksUiState(
            eventSink = wrapEventSink(eventSink)
        )
    }
}