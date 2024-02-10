package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gnoemes.shimori.data.common.ShimoriImage

@Composable
fun PersonCover(
    image: ShimoriImage?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = image,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .size(24.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable { onClick?.invoke() },
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )
    }
}