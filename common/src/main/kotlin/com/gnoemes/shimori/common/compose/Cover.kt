package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.model.rate.RateStatus

@Composable
fun Cover(
    painter: Painter,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    onClick: (() -> Unit)? = null,
    onStatusClick: (() -> Unit)? = null,
    status: RateStatus? = null,
    showAddStatus: Boolean = false,
    contentDescription: String = stringResource(id = R.string.cover)
) {
    Box(
            modifier = modifier

    ) {
        Image(
                modifier = Modifier.apply { if (onClick != null) clickable { onClick() } }
                    .fillMaxSize()
                    .clip(shape)
                ,
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
        )

        //TODO add status btn
    }
}