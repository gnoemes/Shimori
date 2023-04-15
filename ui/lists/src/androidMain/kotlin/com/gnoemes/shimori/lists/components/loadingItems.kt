package com.gnoemes.shimori.lists.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnoemes.shimori.common.ui.LocalShimoriSettings
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.ShimoriChip
import com.gnoemes.shimori.common.ui.shimoriPlaceholder
import com.gnoemes.shimori.common.ui.theme.ShimoriBiggestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.lists.R

@Composable
internal fun LoadingSort() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        val typeInt = LocalShimoriSettings.current.preferredListType.observe
            .collectAsStateWithLifecycle(initialValue = ListType.Anime.type)

        val type = ListType.findOrDefault(typeInt.value)
        val defaultSort = ListSort.defaultForType(type)

        ListSortOption.priorityForType(type).fastForEach { option ->
            val selected = defaultSort.sortOption == option

            ShimoriChip(
                onClick = {},
                modifier = Modifier
                    .shimoriPlaceholder(true)
                    .height(32.dp),
                text = LocalShimoriTextCreator.current.listSortText(type, option),
                selected = selected,
                icon = {
                    if (selected) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_up),
                            contentDescription = null
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
internal fun LoadingItem() {
    Box(
        modifier = Modifier.padding(PaddingValues(horizontal = 16.dp, vertical = 12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.listPosterHeight),
        ) {
            Box(
                modifier = Modifier
                    .shimoriPlaceholder(true)
                    .height(MaterialTheme.dimens.listPosterHeight)
                    .width(MaterialTheme.dimens.listPosterWidth)
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Box(
                    modifier = Modifier
                        .shimoriPlaceholder(
                            visible = true,
                            shape = ShimoriSmallestRoundedCornerShape
                        )
                        .fillMaxWidth()
                        .height(66.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxSize()
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .shimoriPlaceholder(
                                    visible = true,
                                    shape = ShimoriBiggestRoundedCornerShape
                                )
                                .clip(ShimoriBiggestRoundedCornerShape)
                                .size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun LoadingCurrentStatus() {
    val typeInt = LocalShimoriSettings.current.preferredListType.observe
        .collectAsStateWithLifecycle(initialValue = ListType.Anime.type)

    val statusStr = LocalShimoriSettings.current.preferredListStatus.observe
        .collectAsStateWithLifecycle(initialValue = TrackStatus.WATCHING.name)

    val type = ListType.findOrDefault(typeInt.value)
    val status = TrackStatus.valueOf(statusStr.value)

    CurrentStatusItem(
        type = type,
        status = status,
        modifier = Modifier.shimoriPlaceholder(
            visible = true,
            shape = ShimoriSmallestRoundedCornerShape
        )
    )
}