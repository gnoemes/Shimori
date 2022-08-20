package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape
import com.gnoemes.shimori.common.ui.utils.rememberDominantColorState
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
    showStatusButton: Boolean = false,
    isPinned: Boolean = false,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
    ) {
        val dominantColors = rememberDominantColorState(
            defaultColor = MaterialTheme.colorScheme.surfaceVariant,
            defaultOnColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        val imageUrl = image?.preview
        LaunchedEffect(imageUrl, isPinned, status) {
            val needShowDominantColors = isPinned || (showStatusButton && status != null)
            if (imageUrl != null && needShowDominantColors) {
                dominantColors.updateColorsFromImageUrl(imageUrl)
            } else {
                dominantColors.reset()
            }
        }

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
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )

        if (isPinned) {
            Image(
                painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = null,
                colorFilter = ColorFilter.tint(dominantColors.middle),
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp),
            )
        }

        if (showStatusButton) {
            val buttonColors = if (status == null) {
                ShimoriButtonDefaults.buttonColors(
                    containerColor = dominantColors.dominant,
                    contentColor = dominantColors.onDominant
                )
            } else {
                ShimoriButtonDefaults.buttonColors()
            }

            ShimoriCircleButton(
                onClick = { onStatusClick?.invoke() },
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
                    .align(Alignment.TopEnd),
                buttonColors = buttonColors,
                icon = {
                    if (status == null) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null
                        )
                    } else {
                        RateIcon(status)
                    }
                }
            )
        }
    }
}