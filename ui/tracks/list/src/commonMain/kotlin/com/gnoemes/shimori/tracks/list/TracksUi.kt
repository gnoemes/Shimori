package com.gnoemes.shimori.tracks.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.ui.ShimoriSearchBar
import com.gnoemes.shimori.screens.TracksEmptyScreen
import com.gnoemes.shimori.screens.TracksScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
@CircuitInject(screen = TracksScreen::class, scope = UiScope::class)
internal fun TracksUi(
    state: TracksUiState,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val eventSink = state.eventSink

    val uiType = remember(windowSizeClass) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> TracksUiType.Compact
            else -> TracksUiType.Expanded
        }
    }

    TracksUi(
        uiType = uiType,
        openSettings = { eventSink(TracksUiEvent.OpenSettings) },
    )
}

@Composable
private fun TracksUi(
    uiType: TracksUiType,
    openSettings: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedContent(uiType) { uiType ->
                if (uiType == TracksUiType.Compact) {
                    ShimoriSearchBar(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        openSettings = openSettings
                    )
                } else {
                    Row {
                        Spacer(Modifier.weight(1f))

                        ShimoriSearchBar(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .widthIn(max = 328.dp)
                                .fillMaxWidth(),
                            openSettings = openSettings
                        )
                    }
                }
            }
        },
    ) {
        CircuitContent(TracksEmptyScreen)
    }
}

enum class TracksUiType {
    Compact,
    Expanded
}


