package com.gnoemes.shimori.tracks.list.empty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalShimoriIconsUtil
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_list
import com.gnoemes.shimori.common.ui.resources.strings.no_titles
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.screens.TracksEmptyScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
@CircuitInject(screen = TracksEmptyScreen::class, UiScope::class)
internal fun TracksEmptyUi(
    state: TrackEmptyUiState,
    modifier: Modifier = Modifier
) {

    val eventSink = state.eventSink
    TracksEmptyUi(
        state = state,
        openExplore = { eventSink(TracksEmptyUiEvent.OpenExplore(it)) }
    )
}

@Composable
private fun TracksEmptyUi(
    state: TrackEmptyUiState,
    openExplore: (type: TrackTargetType) -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current
    val iconUtil = LocalShimoriIconsUtil.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Icons.ic_list),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(Strings.no_titles),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                state.availableTypes.forEach { type ->
                    AssistChip(
                        modifier = Modifier,
                        onClick = { openExplore(type) },
                        label = {
                            Text(textCreator.name(type))
                        },
                        leadingIcon = {
                            val icon = iconUtil.icon(type)
                            Icon(
                                painter = painterResource(icon),
                                modifier = Modifier.size(18.dp),
                                contentDescription = textCreator.name(type),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        },
                    )
                }
            }
        }
    }
}