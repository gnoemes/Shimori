package com.gnoemes.shimori.lists.edit.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.LocalShimoriTrackUtil
import com.gnoemes.shimori.common.ui.components.RatingBar
import com.gnoemes.shimori.common.ui.components.ShimoriChip
import com.gnoemes.shimori.common.ui.noRippleClickable
import com.gnoemes.shimori.common.ui.theme.ShimoriDefaultRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.lists.edit.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
internal fun Title(
    image: ShimoriImage?,
    text: String,
    progressText: String?
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier.size(24.dp)
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(image)
                    .apply {
                        crossfade(true)
                    }.build(),
                contentDescription = text,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(ShimoriSmallestRoundedCornerShape),
                contentScale = ContentScale.Crop
            )
        }

        val modifier =
            if (progressText.isNullOrEmpty()) Modifier.weight(1f)
            else Modifier

        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        AnimatedVisibility(
            visible = !progressText.isNullOrEmpty(),
        ) {
            Text(
                text = progressText.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
internal fun StatusSelector(
    initialized: Boolean,
    type: TrackTargetType,
    selectedStatus: TrackStatus,
    onStatusChanged: (TrackStatus) -> Unit
) {

    val statuses = TrackStatus.listPagesOrder
    val scrollState = rememberScrollState()

    var initialScrolled by remember { mutableStateOf(false) }

    val offset = with(LocalDensity.current) {
        16.dp.roundToPx()
    }

    val coroutineScope = rememberCoroutineScope()

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
            .horizontalScroll(scrollState)
            .animateContentSize()
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        statuses.forEach { status ->
            ShimoriChip(
                onClick = { onStatusChanged(status) },
                text = LocalShimoriTextCreator.current.trackStatusText(type, status),
                selected = status == selectedStatus,
                modifier = Modifier
                    .height(32.dp)
                    .onGloballyPositioned { coordinates ->
                        if (initialized && !initialScrolled && status == selectedStatus) {
                            coroutineScope.launch {
                                val pixelPosition =
                                    coordinates.positionInParent().x.roundToInt()
                                scrollState.scrollTo(pixelPosition - offset)
                            }
                            initialScrolled = true
                        }
                    },
                icon = {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(
                            LocalShimoriTrackUtil.current.trackStatusIcon(
                                status
                            )
                        ),
                        contentDescription = null
                    )
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
internal fun ProgressBoxes(
    anons: Boolean,
    progress: Int,
    size: Int?,
    onProgressChanged: (Int) -> Unit,
    onProgressEdit: () -> Unit,
    rewatches: Int,
    onRewatchesChanged: (Int) -> Unit,
    onRewatchesEdit: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.width(4.dp))

        Progress(
            anons = anons,
            progress = progress,
            size = size,
            onProgressChanged = onProgressChanged,
            onEditClicked = onProgressEdit
        )

        Rewatches(
            rewatches = rewatches,
            onRewatchesChanged = onRewatchesChanged,
            onEditClicked = onRewatchesEdit
        )

        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
internal fun Progress(
    anons: Boolean,
    progress: Int,
    size: Int?,
    onProgressChanged: (Int) -> Unit,
    onEditClicked: () -> Unit,
) {
    ProgressBox(
        onEditClicked = onEditClicked
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.progress),
                style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(12.dp))

            val progressText = stringResource(
                R.string.progress_format,
                progress,
                size.let { if (it == null || it == 0) "?" else "$it" }
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.primary
                ) {
                    ValueWithIncrementDecrementButtons(
                        progressText = progressText,
                        value = progress,
                        decrementEnabled = progress - 1 >= 0,
                        incrementEnabled = progress + 1 <= (size ?: Integer.MAX_VALUE) && !anons,
                        onDecrementClick = onProgressChanged,
                        onIncrementClick = onProgressChanged,
                    )
                }
            }

        }
    }
}

@Composable
internal fun Rewatches(
    rewatches: Int,
    onRewatchesChanged: (Int) -> Unit,
    onEditClicked: () -> Unit,
) {
    ProgressBox(
        onEditClicked = onEditClicked
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.re_watches),
                style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.primary
                ) {
                    ValueWithIncrementDecrementButtons(
                        progressText = "$rewatches",
                        value = rewatches,
                        decrementEnabled = rewatches - 1 >= 0,
                        incrementEnabled = true,
                        onDecrementClick = onRewatchesChanged,
                        onIncrementClick = onRewatchesChanged,
                    )
                }
            }

        }
    }
}

@Composable
internal fun Rating(
    score: Int?,
    onScoreChanged: (Int?) -> Unit
) {
    RatingBar(
        rating = (score ?: 0) / 2f,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onRatingChanged = { rating -> onScoreChanged((rating * 2f).roundToInt()) }
    )
}


@Composable
internal fun ProgressBox(
    onEditClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    var boxSize by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, ShimoriDefaultRoundedCornerShape)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                ShimoriDefaultRoundedCornerShape
            )
            .height(80.dp)
            .width(240.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .pointerInput(onEditClicked) {
                detectTapGestures(
                    onTap = {
                        if (it.x <= boxSize / 2) {
                            onEditClicked()
                        }
                    }
                )
            }
            .onGloballyPositioned {
                boxSize = it.size.width
            },
        contentAlignment = Alignment.CenterStart
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColorFor(MaterialTheme.colorScheme.surfaceVariant)) {
            content()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun RowScope.ValueWithIncrementDecrementButtons(
    progressText: String,
    value: Int,
    decrementEnabled: Boolean,
    incrementEnabled: Boolean,
    onDecrementClick: (newValue: Int) -> Unit,
    onIncrementClick: (newValue: Int) -> Unit,
) {

    Text(
        text = progressText,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
    )

    Spacer(modifier = Modifier.weight(1f))

    IconButton(
        onClick = { onDecrementClick(value - 1) },
        enabled = decrementEnabled,
        modifier = Modifier.size(32.dp)
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_remove), contentDescription = null)
    }

    Spacer(modifier = Modifier.width(24.dp))

    IconButton(
        onClick = { onIncrementClick(value + 1) },
        enabled = incrementEnabled,
        modifier = Modifier.size(32.dp)
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
    }
}



@Composable
internal fun Note(
    comment: String?,
    onCommentEdit: () -> Unit
) {
    val text =
        if (!comment.isNullOrEmpty()) comment
        else stringResource(id = R.string.add_note)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(horizontal = 16.dp)
            .noRippleClickable { onCommentEdit() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
