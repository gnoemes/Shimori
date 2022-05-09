package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape
import com.gnoemes.shimori.data.core.entities.rate.RateStatus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cover(
    painter: Painter,
    modifier: Modifier = Modifier,
    shape: Shape = ShimoriSmallRoundedCornerShape,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onStatusClick: (() -> Unit)? = null,
    status: RateStatus? = null,
    showAddStatus: Boolean = false,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier

    ) {
        Image(
            modifier = Modifier
                .combinedClickable(
                    onLongClick = onLongClick,
                    onClick = { onClick?.invoke() }
                )
                .fillMaxSize()
                .clip(shape),
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )

        //TODO add status btn
    }
}