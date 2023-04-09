package com.gnoemes.shimori.lists.change.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.LocalShimoriTrackUtil
import com.gnoemes.shimori.common.ui.components.ShimoriChip
import com.gnoemes.shimori.common.ui.components.TrackIcon
import com.gnoemes.shimori.common.ui.navigation.Screen
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.lists.change.section.ListMenuStatusSectionScreenModel
import com.google.accompanist.flowlayout.FlowRow

@Composable
internal fun Screen.StatusSection(
    type: ListType,
    navigateUp: () -> Unit
) {
    val screenModel = rememberScreenModel<Int, ListMenuStatusSectionScreenModel>(
        tag = "section-${type.type}",
        arg = type.type
    )

    val viewState by screenModel.state.collectAsState()

    if (viewState.statuses.isNotEmpty()) {
        StatusSection(
            statuses = viewState.statuses,
            selectedStatus = viewState.selectedStatus,
            sectionType = type,
            onStatusClick = { newStatus ->
                screenModel.onStatusChanged(newStatus)
                navigateUp()
            },
        )
    }
}

@Composable
private fun StatusSection(
    statuses: List<TrackStatus>,
    selectedStatus: TrackStatus?,
    sectionType: ListType,
    onStatusClick: (newStatus: TrackStatus) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = LocalShimoriTrackUtil.current.listTypeName(sectionType),
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        FlowRow(
            crossAxisSpacing = 8.dp,
            mainAxisSpacing = 8.dp
        ) {
            statuses.fastForEach { status ->
                ShimoriChip(
                    onClick = { onStatusClick(status) },
                    modifier = Modifier.height(32.dp),
                    text = LocalShimoriTextCreator.current.trackStatusText(
                        sectionType.trackType!!,
                        status
                    ),
                    selected = status == selectedStatus,
                    icon = {
                        TrackIcon(
                            status,
                            Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}