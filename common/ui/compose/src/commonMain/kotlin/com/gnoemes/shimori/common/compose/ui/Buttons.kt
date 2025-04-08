package com.gnoemes.shimori.common.compose.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import com.gnoemes.shimori.common.compose.LocalShimoriIconsUtil
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.imageloading.allowHardwareSafe
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_add
import com.gnoemes.shimori.common.ui.resources.strings.add
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import com.kmpalette.loader.rememberPainterLoader
import com.kmpalette.rememberDominantColorState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StatusButton(
    title: ShimoriTitleEntity,
    track: Track?,
    openEditTrack: (Long, TrackTargetType) -> Unit,
    modifier: Modifier = Modifier.widthIn(max = 332.dp).height(48.dp).fillMaxWidth()
) {
    val trackExists by remember { derivedStateOf { track != null } }
    val iconUtil = LocalShimoriIconsUtil.current
    val textCreator = LocalShimoriTextCreator.current

    val colors =
        if (trackExists) MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        else {
            val loader = rememberPainterLoader()
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(title.image)
                    .allowHardwareSafe(false)
                    .size(Size.ORIGINAL)
                    .build(),
            )

            val state by painter.state.collectAsState()
            val dominantColorState = rememberDominantColorState(
                loader = loader,
                defaultColor = MaterialTheme.colorScheme.secondaryContainer,
                defaultOnColor = MaterialTheme.colorScheme.onSecondaryContainer
            )

            val isSuccess by remember { derivedStateOf { state is AsyncImagePainter.State.Success } }
            LaunchedEffect(state) {
                if (isSuccess) {
                    dominantColorState.updateFrom(painter)
                }
            }

            dominantColorState.color to dominantColorState.onColor
        }

    val containerColor by animateColorAsState(colors.first)
    val contentColor by animateColorAsState(colors.second)

    FilledTonalButton(
        onClick = { openEditTrack(title.id, title.type) },
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Icon(
            painterResource(track?.status?.let { iconUtil.trackStatusIcon(it) } ?: Icons.ic_add),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            track?.let { textCreator { it.status.name(title.type) } }
                ?: stringResource(Strings.add)
        )

    }
}