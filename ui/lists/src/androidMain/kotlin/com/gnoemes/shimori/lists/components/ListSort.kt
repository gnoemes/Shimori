package com.gnoemes.shimori.lists.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.ShimoriChip
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.lists.R
import com.gnoemes.shimori.lists.sort.ListSortScreenModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
internal fun ListSort(
    screenModel : ListSortScreenModel,
    modifier: Modifier = Modifier
) {
    val state by screenModel.state.collectAsState()

    ListSort(
        modifier = modifier,
        listType = state.listType,
        active = state.activeSort,
        options = state.options,
        onSortChange = { sort, isDescending -> screenModel.onSortChange(sort, isDescending) }
    )
}

@Composable
private fun ListSort(
    modifier: Modifier,
    listType: ListType,
    active: ListSort,
    options: List<ListSortOption>,
    onSortChange: (ListSortOption, Boolean) -> Unit
) {

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val offset = with(LocalDensity.current) {
        16.dp.roundToPx()
    }

    var initialScrolled by remember(active.type) { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .horizontalScroll(scrollState) then modifier
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
                    .height(32.dp)
                    .onGloballyPositioned { coordinates ->
                        if (!initialScrolled && selected) {
                            scope.launch {
                                val pixelPosition =
                                    coordinates.positionInParent().x.roundToInt()
                                scrollState.animateScrollTo(pixelPosition - offset)
                            }
                            initialScrolled = true
                        }
                    }
                ,
                text = LocalShimoriTextCreator.current.listSortText(listType, option),
                selected = selected,
                icon = {
                    if (selected) {
                        Icon(
                            painter = painterResource(
                                id = if (active.isDescending) R.drawable.ic_arrow_down
                                else R.drawable.ic_arrow_up
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}