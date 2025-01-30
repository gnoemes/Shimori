package com.gnoemes.shimori.tracks.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.screens.TracksMenuScreen
import com.slack.circuit.codegen.annotations.CircuitInject

@Composable
@CircuitInject(screen = TracksMenuScreen::class, scope = UiScope::class)
internal fun TracksMenuUi(
    state: TracksMenuUiState,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val eventSink = state.eventSink
}