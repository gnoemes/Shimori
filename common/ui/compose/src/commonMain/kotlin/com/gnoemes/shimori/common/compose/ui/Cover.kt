package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.gnoemes.shimori.common.compose.theme.CharacterCoverRoundedCornerShape
import com.gnoemes.shimori.common.compose.theme.ic_no_image
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_edit
import com.gnoemes.shimori.data.common.ShimoriImage
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileCover(
    image: ShimoriImage?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = image,
            modifier = Modifier
//                .minimumInteractiveComponentSize()
                .size(24.dp)
                .clip(MaterialTheme.shapes.medium)
                .composed {
                    if (onClick != null) {
                        clickable { onClick() }
                    } else Modifier
                },
            error = painterResource(Icons.ic_no_image),
            fallback = painterResource(Icons.ic_no_image),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackCover(
    image: ShimoriImage?,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    showEditButton: Boolean = false,
    onEditClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clip(shape)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(image)
                .apply {
                    crossfade(true)
                }
                .build(),
            error = painterResource(Icons.ic_no_image),
            fallback = painterResource(Icons.ic_no_image),
            modifier = Modifier
                .matchParentSize()
                .combinedClickable(
                    onLongClick = onLongClick,
                    onClick = { onClick?.invoke() }
                ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        if (showEditButton) {
            FilledTonalIconButton(
                onClick = { onEditClick?.invoke() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(Icons.ic_edit),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun CharacterCover(
    image: ShimoriImage?,
    modifier: Modifier = Modifier,
    shape: Shape = CharacterCoverRoundedCornerShape,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(image)
                .apply {
                    crossfade(true)
                }
                .build(),
            error = painterResource(Icons.ic_no_image),
            fallback = painterResource(Icons.ic_no_image),
            modifier = Modifier
                .matchParentSize()
                .combinedClickable(
                    onClick = onClick
                ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

    }
}


@Composable
fun TrailerCover(
    image: String?,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(image)
                .apply {
                    crossfade(true)
                }
                .build(),
            error = painterResource(Icons.ic_no_image),
            fallback = painterResource(Icons.ic_no_image),
            modifier = Modifier
                .matchParentSize()
                .combinedClickable(
                    onClick = onClick
                ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}