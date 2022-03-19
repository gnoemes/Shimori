package com.gnoemes.shimori.lists_change.section

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gnoemes.shimori.common.compose.LocalShimoriRateUtil
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.ui.RateIcon
import com.gnoemes.shimori.common.compose.ui.ShimoriChip
import com.gnoemes.shimori.common.extensions.collectAsStateWithLifecycle
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import com.google.accompanist.flowlayout.FlowRow
import dagger.hilt.android.EntryPointAccessors

@Composable
internal fun StatusSection(
    type: ListType,
    navigateUp: () -> Unit
) {
    StatusSection(
        viewModel = viewModel(
            factory = viewModelFactory(type = type),
            key = "section-$type"
        ),
        navigateUp = navigateUp,
        sectionType = type
    )
}

@Composable
private fun viewModelFactory(type: ListType): ViewModelProvider.Factory {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ListChangeStatusSectionViewModel.ViewModelFactoryProvider::class.java
    ).sectionFactory()

    return ListChangeStatusSectionViewModel.provideFactory(factory, type.type)
}

@Composable
private fun StatusSection(
    viewModel: ListChangeStatusSectionViewModel,
    navigateUp: () -> Unit,
    sectionType: ListType,
) {
    val viewState = viewModel.state.collectAsStateWithLifecycle(initial = null).value

    viewState?.let {
        if (viewState.statuses.isNotEmpty()) {
            StatusSection(
                statuses = viewState.statuses,
                selectedStatus = viewState.selectedStatus,
                sectionType = sectionType,
                onStatusClick = { newStatus ->
                    viewModel.onStatusChanged(newStatus)
                    //TODO restore?
//                    navigateUp()
                },
            )
        }
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