package com.gnoemes.shimori.lists.change.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnoemes.shimori.common.ui.LocalShimoriRateUtil
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.RateIcon
import com.gnoemes.shimori.common.ui.components.ShimoriChip
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.google.accompanist.flowlayout.FlowRow

@Composable
internal fun StatusSection(
    type: ListType,
    navigateUp: () -> Unit
) {
    StatusSection(
        viewModel = shimoriViewModel(
            key = "section-$type",
            args = arrayOf("type" to type.type)
        ),
        navigateUp = navigateUp,
        sectionType = type
    )
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
private fun StatusSection(
    viewModel: ListChangeStatusSectionViewModel,
    navigateUp: () -> Unit,
    sectionType: ListType,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    if (viewState.statuses.isNotEmpty()) {
        StatusSection(
            statuses = viewState.statuses,
            selectedStatus = viewState.selectedStatus,
            sectionType = sectionType,
            onStatusClick = { newStatus ->
                viewModel.onStatusChanged(newStatus)
                navigateUp()
            },
        )
    }
}

@Composable
private fun StatusSection(
    statuses: List<RateStatus>,
    selectedStatus: RateStatus?,
    sectionType: ListType,
    onStatusClick: (newStatus: RateStatus) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = LocalShimoriRateUtil.current.listTypeName(sectionType),
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
                    text = LocalShimoriTextCreator.current.rateStatusText(
                        sectionType.rateType!!,
                        status
                    ),
                    selected = status == selectedStatus,
                    icon = {
                        RateIcon(status)
                    }
                )
            }
        }
    }
}