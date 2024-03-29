package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.LocalShimoriTrackUtil
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.ui.R

@Composable
fun ChevronIcon(
    painter: Painter = painterResource(R.drawable.ic_chevron_right),
) {
    Icon(
        painter = painter,
        contentDescription = null,
    )
}

@Composable
fun TrackIcon(
    trackStatus: TrackStatus,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = LocalShimoriTrackUtil.current.trackStatusIcon(trackStatus)),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun BackIcon(
    navigateUp: () -> Unit,
    painter: Painter = painterResource(R.drawable.ic_back)
) {

    IconButton(onClick = navigateUp) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
fun ListTypeIcon(
    type: ListType
) {
    Icon(
        painter = painterResource(id = LocalShimoriTrackUtil.current.listTypeIcon(type)),
        contentDescription = null
    )
}

@Composable
fun TitleSnackIcon(
    image: ShimoriImage
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .size(24.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                ShimoriSmallestRoundedCornerShape
            )
            .clip(ShimoriSmallestRoundedCornerShape)
    )
}