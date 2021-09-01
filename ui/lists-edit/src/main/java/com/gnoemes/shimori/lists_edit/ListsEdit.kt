package com.gnoemes.shimori.lists_edit

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.compose.theme.caption
import com.gnoemes.shimori.common.compose.theme.subHeadStyle
import com.gnoemes.shimori.model.common.ShimoriImage
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ListsEdit(
    navigateUp: () -> Unit
) {
    ListsEdit(viewModel = hiltViewModel(), navigateUp = navigateUp)
}

@Composable
private fun ListsEdit(
    viewModel: ListsEditViewModel,
    navigateUp: () -> Unit
) {

    val viewState by viewModel.state.collectAsState()

    val submit = { action: ListsEditAction -> viewModel.submitAction(action) }

    ListsEdit(
            viewState = viewState,
            navigateUp = navigateUp,
            onStatusChanged = { submit(ListsEditAction.StatusChanged(it)) },
            onProgressChanged = { submit(ListsEditAction.ProgressChanged(it)) },
            onRewatchesChanged = { submit(ListsEditAction.RewatchesChanged(it)) },
            onScoreChanged = { submit(ListsEditAction.ScoreChanged(it)) },
            onCommentEdit = { submit(ListsEditAction.CommentEdit(it)) },
            onCommentChanged = { submit(ListsEditAction.CommentChanged(it)) },
            onDelete = { submit(ListsEditAction.Delete) },
            onSave = { submit(ListsEditAction.Save) },
            onTogglePin = { submit(ListsEditAction.TogglePin) },
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ListsEdit(
    viewState: ListsEditViewState,
    navigateUp: () -> Unit,
    onStatusChanged: (RateStatus) -> Unit,
    onProgressChanged: (Int) -> Unit,
    onRewatchesChanged: (Int) -> Unit,
    onScoreChanged: (Int?) -> Unit,
    onCommentEdit: (Boolean) -> Unit,
    onCommentChanged: (String?) -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    onTogglePin: () -> Unit,
) {

    Column {
        BottomSheetThumb()
        Title(image = viewState.image, text = viewState.name)

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(viewState.commentEdit) { editing ->
            if (editing) {

            } else {
                Column {
                    StatusSelector(
                            initialized = viewState.name.isNotEmpty(),
                            type = viewState.type,
                            selectedStatus = viewState.status,
                            onStatusChanged = onStatusChanged
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Progress(
                            progress = viewState.progress,
                            size = viewState.size,
                            onProgressChanged = onProgressChanged
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Rewatches(
                            rewatches = viewState.rewatches,
                            onRewatchesChanged = onRewatchesChanged
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Rating(
                            score = viewState.score,
                            onScoreChanged = onScoreChanged
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun Title(
    image: ShimoriImage?,
    text: String
) {
    Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Box(
                modifier = Modifier.size(24.dp)
        ) {
            Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp)),
                    painter = rememberImagePainter(image),
                    contentDescription = text,
                    contentScale = ContentScale.Crop
            )
        }

        Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = MaterialTheme.typography.subHeadStyle,
                color = MaterialTheme.colors.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatusSelector(
    initialized: Boolean,
    type: RateTargetType,
    selectedStatus: RateStatus,
    onStatusChanged: (RateStatus) -> Unit
) {

    val statuses = RateStatus.listPagesOrder
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
        Spacer(modifier = Modifier.width(16.dp))

        statuses.forEach { status ->
            StatusChip(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
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
                    selected = status == selectedStatus,
                    text = LocalShimoriTextCreator.current.rateStatusText(type, status),
                    iconResId = LocalShimoriRateUtil.current.rateStatusIcon(status),
                    onClick = { onStatusChanged(status) }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
private fun StatusChip(
    modifier: Modifier,
    selected: Boolean,
    text: String,
    @DrawableRes iconResId: Int,
    onClick: () -> Unit
) {
    ShimoriButton(
            selected = selected,
            onClick = onClick,
            modifier = modifier,
            text = text,
            painter = painterResource(iconResId),
            iconSize = 16.dp
    )
}

@Composable
private fun Progress(
    progress: Int,
    size: Int?,
    onProgressChanged: (Int) -> Unit
) {
    OutlineBox {

        val leftText =
            if (progress == size) null
            else stringResource(id = R.string.left_format, size?.let { it - progress } ?: "?")

        ValueWithIncrementDecrementButtons(
                valueTitle = stringResource(id = R.string.progress),
                leftText = leftText,
                value = progress,
                valueLimit = size ?: Integer.MAX_VALUE,
                decrementEnabled = progress - 1 >= 0,
                incrementEnabled = progress + 1 <= size ?: Integer.MAX_VALUE,
                onDecrementClick = onProgressChanged,
                onIncrementClick = onProgressChanged,
                onValueChanged = onProgressChanged,
        )
    }
}

@Composable
private fun Rewatches(
    rewatches: Int,
    onRewatchesChanged: (Int) -> Unit
) {
    OutlineBox {
        ValueWithIncrementDecrementButtons(
                valueTitle = stringResource(id = R.string.re_watches),
                leftText = null,
                value = rewatches,
                valueLimit = Integer.MAX_VALUE,
                decrementEnabled = rewatches - 1 >= 0,
                incrementEnabled = true,
                onDecrementClick = onRewatchesChanged,
                onIncrementClick = onRewatchesChanged,
                onValueChanged = onRewatchesChanged,
        )
    }
}

@Composable
private fun Rating(
    score: Int?,
    onScoreChanged: (Int?) -> Unit
) {

    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
    ) {


//        RatingBar(
//                rating = (score ?: 0) / 2f,
//                modifier = Modifier.height(40.dp)
//        )

        Spacer(modifier = Modifier.width(8.dp))

        val text = score?.let { "$score" } ?: "-"

        Text(
                text = text,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondary
        )

    }
}


@Composable
private fun OutlineBox(
    content: @Composable RowScope.() -> Unit
) {
    Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(56.dp)
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
    ) {

        Row(
                verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun RowScope.ValueWithIncrementDecrementButtons(
    valueTitle: String,
    value: Int,
    valueLimit: Int,
    leftText: String? = null,
    decrementEnabled: Boolean,
    incrementEnabled: Boolean,
    onDecrementClick: (newValue: Int) -> Unit,
    onIncrementClick: (newValue: Int) -> Unit,
    onValueChanged: (newValue: Int) -> Unit,
) {
    Text(
            text = valueTitle,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.caption
    )

    Spacer(modifier = Modifier.width(8.dp))

    BasicTextField(
            value = "$value",
            textStyle = MaterialTheme.typography.subHeadStyle.copy(color = MaterialTheme.colors.secondary),
            singleLine = true,
            onValueChange = { text ->
                val nums = text.replace(numberRegex, "")
                val intValue = nums.toIntOrNull()
                if (nums.isNotEmpty() && nums.length <= 4 && intValue != null && intValue <= valueLimit) {
                    onValueChanged(intValue)
                } else if (nums.isEmpty()) {
                    onValueChanged(0)
                } else if (intValue != null && intValue > valueLimit) {
                    onValueChanged(valueLimit)
                }

            },
            modifier = Modifier
                .width(43.dp),
            cursorBrush = SolidColor(MaterialTheme.colors.secondary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )

    Spacer(modifier = Modifier.weight(1f))

    AnimatedVisibility(visible = leftText != null) {
        Text(
                text = leftText.orEmpty(),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.caption,
        )
    }

    if (leftText != null) {
        Spacer(modifier = Modifier.width(16.dp))
    }

    ShimoriIconButton(
            onClick = { onDecrementClick(value - 1) },
            selected = false,
            enabled = decrementEnabled,
            painter = painterResource(id = R.drawable.ic_remove),
            modifier = Modifier
                .size(32.dp)
    )

    Spacer(modifier = Modifier.width(16.dp))

    ShimoriIconButton(
            onClick = { onIncrementClick(value + 1) },
            selected = false,
            enabled = incrementEnabled,
            painter = painterResource(id = R.drawable.ic_add),
            modifier = Modifier
                .size(32.dp)
    )
}

private val numberRegex = "(?![0-9]{1,4})".toRegex()