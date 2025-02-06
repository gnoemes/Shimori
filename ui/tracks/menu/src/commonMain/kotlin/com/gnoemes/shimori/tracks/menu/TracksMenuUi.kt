package com.gnoemes.shimori.tracks.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalShimoriIconsUtil
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.compose.isMedium
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_clear
import com.gnoemes.shimori.common.ui.resources.strings.lists_title
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.screens.TracksMenuScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@CircuitInject(screen = TracksMenuScreen::class, scope = UiScope::class)
internal fun TracksMenuUi(
    state: TracksMenuUiState,
    modifier: Modifier = Modifier,
) {

    val widthSizeClass = LocalWindowSizeClass.current.widthSizeClass
    val eventSink = state.eventSink

    TracksMenuUi(
        widthSizeClass = widthSizeClass,
        selectedType = state.selectedType,
        selectedStatus = state.selectedStatus,
        availableStatuses = state.availableStatuses,
        openStatus = { type, status -> eventSink(TracksMenuUiEvent.OpenStatus(type, status)) },
        navigateUp = { eventSink(TracksMenuUiEvent.NavigateUp) }
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
private fun TracksMenuUi(
    widthSizeClass: WindowWidthSizeClass,
    selectedType: TrackTargetType,
    selectedStatus: TrackStatus,
    availableStatuses: Map<TrackTargetType, List<TrackStatus>>,
    openStatus: (TrackTargetType, TrackStatus) -> Unit,
    navigateUp: () -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current
    val icons = LocalShimoriIconsUtil.current


    Column(
        modifier = Modifier.composed {
            if (widthSizeClass.isCompact()) Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
            else if (widthSizeClass.isMedium())
                Modifier.fillMaxSize()
                    .padding(
                        start = 24.dp,
                        top = 24.dp,
                        bottom = 16.dp,
                        end = 12.dp
                    )
            else Modifier.fillMaxSize()
                .padding(16.dp)
        }

    ) {

        if (widthSizeClass.isCompact()) {
            Text(
                stringResource(Strings.lists_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        } else {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(Strings.lists_title),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )

                    if (widthSizeClass.isMedium()) {
                        IconButton(navigateUp) {
                            Icon(painterResource(Icons.ic_clear), contentDescription = null)
                        }
                    }
                }
            }
        }

        availableStatuses.forEach { (type, statuses) ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                textCreator {
                    type.nameMenu()
                },
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize provides 0.dp
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statuses.forEach { status ->
                        val isSelected = selectedType == type && selectedStatus == status
                        FilterChip(
                            selected = isSelected,
                            onClick = { openStatus(type, status) },
                            label = {
                                Text(
                                    textCreator {
                                        status.name(type)
                                    },
                                )
                            },
                            leadingIcon = {
                                AnimatedVisibility(isSelected) {
                                    Icon(
                                        painterResource(icons.trackStatusIcon(status)),
                                        contentDescription = null,
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}