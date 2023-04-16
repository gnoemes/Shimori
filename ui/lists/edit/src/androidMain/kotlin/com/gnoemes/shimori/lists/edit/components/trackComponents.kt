@file:OptIn(ExperimentalLayoutApi::class)

package com.gnoemes.shimori.lists.edit.components

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.ButtonRatingBar
import com.gnoemes.shimori.common.ui.components.ConfirmationButtonType
import com.gnoemes.shimori.common.ui.components.NavigationCard
import com.gnoemes.shimori.common.ui.components.NavigationCardDivider
import com.gnoemes.shimori.common.ui.components.NavigationCardItem
import com.gnoemes.shimori.common.ui.components.ShimoriCircleButton
import com.gnoemes.shimori.common.ui.components.ShimoriConformationButton
import com.gnoemes.shimori.common.ui.components.TrackIcon
import com.gnoemes.shimori.common.ui.imeAsState
import com.gnoemes.shimori.common.ui.noRippleClickable
import com.gnoemes.shimori.common.ui.theme.ShimoriDefaultRoundedCornerShape
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.lists.edit.R
import com.gnoemes.shimori.lists.edit.TrackEditInputState
import kotlin.math.roundToInt


@Composable
internal fun StatusSelector(
    type: TrackTargetType,
    selectedStatus: TrackStatus,
    onStatusChanged: (TrackStatus) -> Unit
) {
    val statuses = TrackStatus.listPagesOrder
    val textCreator = LocalShimoriTextCreator.current

    NavigationCard(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        statuses.fastForEachIndexed { index, status ->
            if (index != 0) NavigationCardDivider()

            NavigationCardItem(
                title = textCreator.trackStatusText(type, status),
                subTitle = if (selectedStatus == status) stringResource(id = R.string.List_item_current_state) else null,
                icon = {
                    TrackIcon(trackStatus = status)
                },
                onClick = {
                    onStatusChanged(status)
                }
            )
        }
    }
}

@Composable
internal fun ProgressBoxes(
    inputState: TrackEditInputState,
    anons: Boolean,
    progress: Int,
    size: Int?,
    onProgressChanged: (Int) -> Unit,
    onProgressEdit: () -> Unit,
    rewatches: Int,
    onRewatchesChanged: (Int) -> Unit,
    onRewatchesEdit: () -> Unit,
    onDefaultInputState: () -> Unit,
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
            inputState = inputState,
            anons = anons,
            progress = progress,
            size = size,
            onProgressChanged = onProgressChanged,
            onEditClicked = onProgressEdit,
            onDefaultInputState = onDefaultInputState
        )

        Rewatches(
            inputState = inputState,
            rewatches = rewatches,
            onRewatchesChanged = onRewatchesChanged,
            onEditClicked = onRewatchesEdit,
            onDefaultInputState = onDefaultInputState
        )

        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
internal fun RowScope.Progress(
    inputState: TrackEditInputState,
    anons: Boolean,
    progress: Int,
    size: Int?,
    onProgressChanged: (Int) -> Unit,
    onEditClicked: () -> Unit,
    onDefaultInputState: () -> Unit,
) {
    ProgressBox(
        modifier = Modifier
            .weight(1f)
            .height(116.dp),
        onEditClicked = onEditClicked
    ) {
        var progressState by remember(progress) { mutableStateOf(progress) }

        val isKeyboardVisible by imeAsState()
        var prevKeyboardState by remember(inputState) { mutableStateOf(false) }

        val onCommit = {
            onProgressChanged(progressState)
            onDefaultInputState()
        }

        //commit if keyboard closed
        if (inputState == TrackEditInputState.Progress
            && prevKeyboardState != isKeyboardVisible
            && !isKeyboardVisible
        ) {
            onCommit()
        }

        Column {
            Text(
                text = stringResource(id = R.string.progress),
                style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            val progressText = stringResource(
                R.string.progress_format,
                progress,
                size.let { if (it == null || it == 0) "?" else "$it" }
            )

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.primary
            ) {
                ValueWithIncrementDecrementButtons(
                    value = progressState,
                    decrementEnabled = progressState - 1 >= 0,
                    incrementEnabled = progressState + 1 <= (size ?: Integer.MAX_VALUE) && !anons,
                    onDecrementClick = onProgressChanged,
                    onIncrementClick = onProgressChanged,
                ) {
                    if (inputState == TrackEditInputState.Progress) {
                        ProgressInput(
                            progress = progressState,
                            size = size,
                            onChangedLocal = { progressState = it },
                            onCommit = onCommit
                        )

                        prevKeyboardState = isKeyboardVisible
                    } else {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = progressText,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun RowScope.Rewatches(
    inputState: TrackEditInputState,
    rewatches: Int,
    onRewatchesChanged: (Int) -> Unit,
    onEditClicked: () -> Unit,
    onDefaultInputState: () -> Unit,
) {
    ProgressBox(
        modifier = Modifier
            .width(112.dp)
            .height(116.dp),
        onEditClicked = onEditClicked
    ) {
        var rewatchesState by remember(rewatches) { mutableStateOf(rewatches) }

        val isKeyboardVisible by imeAsState()
        var prevKeyboardState by remember(inputState) { mutableStateOf(false) }

        val onCommit = {
            onRewatchesChanged(rewatchesState)
            onDefaultInputState()
        }

        //commit if keyboard closed
        if (inputState == TrackEditInputState.Rewatching
            && prevKeyboardState != isKeyboardVisible
            && !isKeyboardVisible
        ) {
            onCommit()
        }

        Column {
            Text(
                text = stringResource(id = R.string.re_watches),
                style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.primary
            ) {
                ValueWithIncrementDecrementButtons(
                    value = rewatchesState,
                    decrementEnabled = rewatchesState - 1 >= 0,
                    incrementEnabled = true,
                    onDecrementClick = onRewatchesChanged,
                    onIncrementClick = onRewatchesChanged,
                ) {
                    if (inputState == TrackEditInputState.Rewatching) {
                        RewatchesInput(
                            rewatches = rewatchesState,
                            onChangedLocal = { rewatchesState = it },
                            onCommit = onCommit
                        )

                        prevKeyboardState = isKeyboardVisible
                    } else {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "$rewatchesState",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
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
    ButtonRatingBar(
        rating = score?.toFloat() ?: 0f,
        colorNotFilled = MaterialTheme.colorScheme.surfaceVariant,
        colorFilled = MaterialTheme.colorScheme.secondary,
        painterNotFilled = painterResource(id = R.drawable.ic_star_rating),
        painterFilled = painterResource(id = R.drawable.ic_star_rating_filled),
        starSize = 32.dp,
        modifier = Modifier
            .height(32.dp)
            .padding(horizontal = 12.dp),
        onRatingChanged = { rating -> onScoreChanged((rating).roundToInt()) }
    )
}


@Composable
internal fun ProgressBox(
    modifier: Modifier,
    onEditClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    var boxSize by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                ShimoriDefaultRoundedCornerShape
            )
                then modifier
                then Modifier
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

@Composable
internal fun ColumnScope.ValueWithIncrementDecrementButtons(
    value: Int,
    decrementEnabled: Boolean,
    incrementEnabled: Boolean,
    onDecrementClick: (newValue: Int) -> Unit,
    onIncrementClick: (newValue: Int) -> Unit,
    content: @Composable () -> Unit
) {
    content()

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        ShimoriCircleButton(
            onClick = { onDecrementClick(value - 1) },
            enabled = decrementEnabled,
            modifier = Modifier
                .size(32.dp),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_remove),
                    contentDescription = null
                )
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        ShimoriCircleButton(
            onClick = { onIncrementClick(value + 1) },
            enabled = incrementEnabled,
            modifier = Modifier
                .size(32.dp),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null
                )
            }
        )
    }
}

@Composable
internal fun NoteHint() {
    Text(
        text = stringResource(id = R.string.title_note_hint),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
internal fun Note(
    inputState: TrackEditInputState,
    comment: String?,
    onCommentChanged: (String?) -> Unit,
    onCommentEdit: () -> Unit,
    onDefaultInputState: () -> Unit
) {
    val text =
        if (!comment.isNullOrEmpty()) comment
        else stringResource(id = R.string.add_note)

    var commentState by remember(comment) { mutableStateOf(comment) }

    val isKeyboardVisible by imeAsState()
    var prevKeyboardState by remember(inputState) { mutableStateOf(false) }

    val onCommit = {
        onCommentChanged(commentState)
        onDefaultInputState()
    }

    //commit if keyboard closed
    if (inputState == TrackEditInputState.Comment
        && prevKeyboardState != isKeyboardVisible
        && !isKeyboardVisible
    ) {
        onCommit()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(horizontal = 16.dp)
            .imePadding()
            .noRippleClickable { onCommentEdit() }
    ) {
        Column {
            NoteHint()
            Spacer(modifier = Modifier.height(12.dp))

            if (inputState == TrackEditInputState.Comment) {
                CommentInput(
                    commentState,
                    onChangedLocal = { commentState = it },
                    onCommit = onCommit
                )

                prevKeyboardState = isKeyboardVisible
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
internal fun BottomBar(
    modifier: Modifier,
    newTrack: Boolean,
    pinned: Boolean,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    onTogglePin: () -> Unit
) {
    Surface(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp),
                enabled = !newTrack
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null
                )
            }

            ShimoriConformationButton(
                onClick = onSave,
                text = stringResource(id = if (newTrack) R.string.add else R.string.save),
                type = ConfirmationButtonType.Primary,
                modifier = Modifier
                    .weight(1f)
                    .height(IntrinsicSize.Min)
            )

            IconButton(
                onClick = onTogglePin,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pin),
                    contentDescription = null,
                    tint =
                    if (pinned) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }

        }
    }
}

