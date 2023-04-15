package com.gnoemes.shimori.lists.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.LocalShimoriTrackUtil
import com.gnoemes.shimori.common.ui.components.Divider
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus

@Composable
internal fun CurrentStatusItem(
    type: ListType,
    status: TrackStatus,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(
        start = 16.dp,
        top = 12.dp,
        end = 16.dp,
        bottom = 4.dp
    )
) {
    val trackUtil = LocalShimoriTrackUtil.current
    val textCreator = LocalShimoriTextCreator.current

    Text(
        modifier = Modifier.padding(paddingValues) then modifier,
        text = buildAnnotatedString {
            if (type == ListType.Pinned) append(trackUtil.listTypeName(type))
            else {
                append(textCreator.trackStatusText(type.trackType!!, status))
                append(Divider)
                append(trackUtil.listTypeName(type))
            }
        },
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.labelMedium
    )
}