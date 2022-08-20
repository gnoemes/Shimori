package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.ui.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cover(
    image: ShimoriImage?,
    modifier: Modifier = Modifier,
    shape: Shape = ShimoriSmallRoundedCornerShape,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onStatusClick: (() -> Unit)? = null,
    status: RateStatus? = null,
    showAddStatus: Boolean = false,
    isPinned: Boolean = false,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier

    ) {

        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(image)
                .apply {
                    crossfade(true)
                }.build(),
            modifier = Modifier
                .combinedClickable(
                    onLongClick = onLongClick,
                    onClick = { onClick?.invoke() }
                )
                .fillMaxSize()
                .clip(shape),
            contentDescription =contentDescription,
            contentScale = ContentScale.Crop
        )

        if (isPinned) {
            Image(
                painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp)
            )
        }

        //TODO add status btn
    }
}