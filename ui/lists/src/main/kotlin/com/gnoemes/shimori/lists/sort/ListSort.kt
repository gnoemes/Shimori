package com.gnoemes.shimori.lists.sort

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.ShimoriChip
import com.gnoemes.shimori.common.extensions.rememberStateWithLifecycle
import com.gnoemes.shimori.lists.R
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption

@Composable
internal fun ListSort() {
    ListSort(viewModel = hiltViewModel())
}

@Composable
private fun ListSort(
    viewModel: ListSortViewModel
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    ListSort(
        listType = state.listType,
        active = state.activeSort,
        options = state.options,
        onSortChange = { sort, isDescending -> viewModel.onSortChange(sort, isDescending) }
    )
}

@Composable
private fun ListSort(
    listType: ListType,
    active: RateSort,
    options: List<RateSortOption>,
    onSortChange: (RateSortOption, Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        options.fastForEach { option ->
            val selected = active.sortOption == option

            ShimoriChip(
                onClick = {
                    if (selected) onSortChange(option, !active.isDescending)
                    else onSortChange(option, false)
                },
                modifier = Modifier
                    .height(32.dp),
                text = LocalShimoriTextCreator.current.listSortText(listType, option),
                selected = selected,
                icon = {
                    if (selected) {
                        Icon(
                            painter = painterResource(
                                id = if (active.isDescending) R.drawable.ic_arrow_down
                                else R.drawable.ic_arrow_up
                            ),
                            contentDescription = null
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}