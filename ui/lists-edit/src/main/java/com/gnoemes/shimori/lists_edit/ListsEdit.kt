package com.gnoemes.shimori.lists_edit

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.compose.theme.caption
import com.gnoemes.shimori.common.compose.theme.disabled
import com.gnoemes.shimori.common.compose.theme.subHeadStyle
import com.gnoemes.shimori.model.common.ShimoriImage
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
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
            onProgressEdit = { submit(ListsEditAction.ProgressInput) },
            onRewatchesEdit = { submit(ListsEditAction.RewatchingInput) },
            onCommentEdit = { submit(ListsEditAction.CommentInput) },
            onDefaultInputState = { submit(ListsEditAction.NoneInput) },
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
    onProgressEdit: () -> Unit,
    onRewatchesEdit: () -> Unit,
    onCommentEdit: () -> Unit,
    onDefaultInputState: () -> Unit,
    onCommentChanged: (String?) -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    onTogglePin: () -> Unit,
) {

    val offset = remember { mutableStateOf(0) }

    SheetLayout(
            modifier = Modifier.navigationBarsWithImePadding(),
            offset = offset,
            bottomBar = {
                if (viewState.inputState == ListEditInputState.None) {
                    BottomBar(
                            modifier = Modifier
                                .height(96.dp)
                                .align(Alignment.BottomCenter)
                                .offset { IntOffset(0, offset.value) },
                            newRate = viewState.newRate,
                            pinned = viewState.pinned,
                            onDelete = onDelete,
                            onSave = onSave,
                            onTogglePin = onTogglePin
                    )
                }
            }
    ) {
        Column {
            BottomSheetThumb()

            val title = when (viewState.inputState) {
                ListEditInputState.Progress -> stringResource(id = R.string.progress)
                ListEditInputState.Rewatching -> stringResource(id = R.string.re_watches)
                else -> viewState.name
            }

            var progress by remember(viewState.progress) { mutableStateOf(viewState.progress) }

            val progressText =
                when {
                    viewState.inputState != ListEditInputState.Progress -> null
                    progress == viewState.size -> null
                    else -> stringResource(
                            id = R.string.left_format,
                            viewState.size?.let { it - progress } ?: "?"
                    )
                }

            Title(
                    image = viewState.image,
                    text = title,
                    progressText = progressText
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(viewState.inputState) { state ->
                when (state) {
                    ListEditInputState.Progress -> {
                        Column(
                                Modifier.padding(horizontal = 16.dp)
                        ) {
                            ProgressInput(
                                    progress = progress,
                                    size = viewState.size,
                                    onChangedLocal = { progress = it },
                                    onCommit = { onProgressChanged(progress) }
                            )

                            EditButtons(
                                    onDefaultInputState = {
                                        onDefaultInputState()
                                        progress = viewState.progress
                                    },
                                    onChangeAccept = {
                                        onProgressChanged(progress)
                                        onDefaultInputState()
                                    },
                            )
                        }
                    }
                    ListEditInputState.Rewatching -> {
                        Column(
                                Modifier.padding(horizontal = 16.dp)
                        ) {
                            var rewatches by remember(viewState.rewatches) { mutableStateOf(viewState.rewatches) }

                            RewatchesInput(
                                    rewatches = rewatches,
                                    onChangedLocal = { rewatches = it },
                                    onCommit = { onRewatchesChanged(rewatches) }
                            )

                            EditButtons(
                                    onDefaultInputState = onDefaultInputState,
                                    onChangeAccept = {
                                        onRewatchesChanged(rewatches)
                                        onDefaultInputState()
                                    },
                            )
                        }
                    }
                    ListEditInputState.Comment -> {
                        Column(
                                Modifier.padding(horizontal = 16.dp)
                        ) {
                            var comment by remember(viewState.comment) { mutableStateOf(viewState.comment) }

                            CommentInput(
                                    comment,
                                    onChangedLocal = { comment = it },
                                    onCommit = { onCommentChanged(comment) }
                            )

                            EditButtons(
                                    onDefaultInputState = onDefaultInputState,
                                    onChangeAccept = {
                                        onCommentChanged(comment)
                                        onDefaultInputState()
                                    },
                            )
                        }
                    }
                    else -> {
                        Column(
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            StatusSelector(
                                    initialized = viewState.name.isNotEmpty(),
                                    type = viewState.type,
                                    selectedStatus = viewState.status,
                                    onStatusChanged = onStatusChanged
                            )

                            ProgressBoxes(
                                    progress = viewState.progress,
                                    size = viewState.size,
                                    onProgressChanged = onProgressChanged,
                                    onProgressEdit = onProgressEdit,
                                    rewatches = viewState.rewatches,
                                    onRewatchesChanged = onRewatchesChanged,
                                    onRewatchesEdit = onRewatchesEdit
                            )

                            Rating(
                                    score = viewState.score,
                                    onScoreChanged = onScoreChanged
                            )


                            Note(
                                    comment = viewState.comment,
                                    onCommentEdit = onCommentEdit
                            )

                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@OptIn(ExperimentalCoilApi::class)
@Composable
private fun Title(
    image: ShimoriImage?,
    text: String,
    progressText: String?
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

        val modifier =
            if (progressText.isNullOrEmpty()) Modifier.weight(1f)
            else Modifier

        Text(
                modifier = modifier,
                text = text,
                style = MaterialTheme.typography.subHeadStyle,
                color = MaterialTheme.colors.onPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
        )

        AnimatedVisibility(visible = !progressText.isNullOrEmpty()) {
            Text(
                    text = progressText.orEmpty(),
                    style = MaterialTheme.typography.subHeadStyle,
                    color = MaterialTheme.colors.disabled,
            )
        }
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
private fun ProgressBoxes(
    progress: Int,
    size: Int?,
    onProgressChanged: (Int) -> Unit,
    onProgressEdit: () -> Unit,
    rewatches: Int,
    onRewatchesChanged: (Int) -> Unit,
    onRewatchesEdit: () -> Unit
) {
    Column {
        Progress(
                progress = progress,
                size = size,
                onProgressChanged = onProgressChanged,
                onEditClicked = onProgressEdit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Rewatches(
                rewatches = rewatches,
                onRewatchesChanged = onRewatchesChanged,
                onEditClicked = onRewatchesEdit
        )
    }
}

@Composable
private fun Progress(
    progress: Int,
    size: Int?,
    onProgressChanged: (Int) -> Unit,
    onEditClicked: () -> Unit,
) {


    OutlineBox(onEditClicked) {

        val leftText =
            if (progress == size) null
            else stringResource(id = R.string.left_format, size?.let { it - progress } ?: "?")

        ValueWithIncrementDecrementButtons(
                valueTitle = stringResource(id = R.string.progress),
                leftText = leftText,
                value = progress,
                decrementEnabled = progress - 1 >= 0,
                incrementEnabled = progress + 1 <= size ?: Integer.MAX_VALUE,
                onDecrementClick = onProgressChanged,
                onIncrementClick = onProgressChanged,
        )
    }
}

@Composable
private fun Rewatches(
    rewatches: Int,
    onRewatchesChanged: (Int) -> Unit,
    onEditClicked: () -> Unit,
) {
    OutlineBox(onEditClicked) {
        ValueWithIncrementDecrementButtons(
                valueTitle = stringResource(id = R.string.re_watches),
                leftText = null,
                value = rewatches,
                decrementEnabled = rewatches - 1 >= 0,
                incrementEnabled = true,
                onDecrementClick = onRewatchesChanged,
                onIncrementClick = onRewatchesChanged,
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
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.weight(1f))

        RatingBar(
                rating = (score ?: 0) / 2f,
                modifier = Modifier
                    .height(48.dp),
                onRatingChanged = { rating -> onScoreChanged((rating * 2f).roundToInt()) }
        )

        Spacer(modifier = Modifier.width(8.dp))

        val text = score?.let { "$score" } ?: "-"

        Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .noRippleClickable {
                        val newScore = when (score) {
                            10 -> 0
                            else -> (score ?: 0) + 1
                        }
                        onScoreChanged(newScore)
                    },
                contentAlignment = Alignment.Center
        ) {
            Text(
                    text = text,
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.secondary,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

    }
}


@Composable
private fun OutlineBox(
    onEditClicked: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    var boxSize by remember { mutableStateOf(0) }

    Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(56.dp)
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .pointerInput(onEditClicked) {
                    detectTapGestures(
                            onPress = {
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
    leftText: String? = null,
    decrementEnabled: Boolean,
    incrementEnabled: Boolean,
    onDecrementClick: (newValue: Int) -> Unit,
    onIncrementClick: (newValue: Int) -> Unit,
) {

    Text(
            text = valueTitle,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.caption,
    )

    Spacer(modifier = Modifier.width(8.dp))

    Text(
            text = "$value",
            style = MaterialTheme.typography.subHeadStyle,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.width(43.dp)
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

@Composable
private fun BottomBar(
    modifier: Modifier,
    newRate: Boolean,
    pinned: Boolean,
    onDelete: () -> Unit,
    onSave: () -> Unit,
    onTogglePin: () -> Unit
) {
    Surface(
            modifier = modifier,
            elevation = 0.dp
    ) {
        Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {

            ShimoriIconButton(
                    onClick = onDelete,
                    selected = false,
                    painter = painterResource(id = R.drawable.ic_delete),
                    modifier = Modifier.size(32.dp),
                    enabled = !newRate
            )

            ShimoriConfirmationButton(
                    onClick = onSave,
                    text = stringResource(id = if (newRate) R.string.add else R.string.save),
                    modifier = Modifier
                        .weight(1f)
                        .height(IntrinsicSize.Min)
            )

            ShimoriIconButton(
                    onClick = onTogglePin,
                    selected = pinned,
                    painter = painterResource(id = R.drawable.ic_pin),
                    modifier = Modifier.size(32.dp),
            )

        }
    }
}

@Composable
private fun Note(
    comment: String?,
    onCommentEdit: () -> Unit
) {

    val text =
        if (!comment.isNullOrEmpty()) comment
        else stringResource(id = R.string.add_note)

    EnlargedButton(
            painter = painterResource(id = R.drawable.ic_note),
            selected = false,
            onClick = onCommentEdit,
            text = text,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(48.dp)
                .fillMaxWidth(),
    )
}

@Composable
private fun ProgressInput(
    progress: Int,
    size: Int?,
    onChangedLocal: (Int) -> Unit,
    onCommit: () -> Unit
) {
    NumberInput(
            value = progress,
            limit = size,
            onChangedLocal = onChangedLocal,
            onCommit = onCommit
    )
}

@Composable
private fun RewatchesInput(
    rewatches: Int,
    onChangedLocal: (Int) -> Unit,
    onCommit: () -> Unit
) {
    NumberInput(
            value = rewatches,
            limit = null,
            onChangedLocal = onChangedLocal,
            onCommit = onCommit
    )
}

@Composable
private fun NumberInput(
    value: Int,
    limit: Int?,
    onChangedLocal: (Int) -> Unit,
    onCommit: () -> Unit
) {
    val valueLimit = limit ?: Integer.MAX_VALUE

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    ShimoriTextField(
            value = "$value",
            textStyle = MaterialTheme.typography.caption,
            textColor = MaterialTheme.colors.secondary,
            onValueChange = { text ->
                val nums = text.replace(numberRegex, "")
                val intValue = nums.toIntOrNull()
                if (nums.isNotEmpty() && nums.length <= 4 && intValue != null && intValue <= valueLimit) {
                    onChangedLocal(intValue)
                } else if (nums.isEmpty()) {
                    onChangedLocal(0)
                } else if (intValue != null && intValue > valueLimit) {
                    onChangedLocal(valueLimit)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 10.dp, max = 150.dp)
                .focusRequester(focusRequester),
            cursorColor = MaterialTheme.colors.secondary,
            keyboardActions = KeyboardActions(onDone = { onCommit() }),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Composable
private fun CommentInput(
    comment: String?,
    onChangedLocal: (String) -> Unit,
    onCommit: () -> Unit
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
    }

    ShimoriTextField(
            value = comment.orEmpty(),
            textStyle = MaterialTheme.typography.caption,
            textColor = MaterialTheme.colors.onPrimary,
            onValueChange = onChangedLocal,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 10.dp, max = 150.dp)
                .focusRequester(focusRequester),
            cursorColor = MaterialTheme.colors.secondary,
            keyboardActions = KeyboardActions(onDone = { onCommit() }),
            hint = {
                Text(
                        text = stringResource(id = R.string.add_note),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.disabled
                )
            }
    )
}

@Composable
private fun EditButtons(
    onDefaultInputState: () -> Unit,
    onChangeAccept: () -> Unit
) {
    Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        ShimoriIconButton(
                onClick = onDefaultInputState,
                selected = false,
                painter = painterResource(id = R.drawable.ic_back),
                modifier = Modifier.size(32.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        ShimoriIconButton(
                onClick = onChangeAccept,
                selected = false,
                painter = painterResource(id = R.drawable.ic_completed),
                modifier = Modifier.size(32.dp),
        )
    }
}

@Composable
private fun SheetLayout(
    modifier: Modifier = Modifier,
    offset: MutableState<Int>,
    bottomBar: @Composable BoxWithConstraintsScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    var parentHeight by remember { mutableStateOf(0) }
    val navigationBarHeight = LocalWindowInsets.current.navigationBars.bottom

    BoxWithConstraints(
            modifier = modifier then Modifier
                .onGloballyPositioned {
                    parentHeight = it.size.height
                }
    ) {

        content()

        val screenHeight = constraints.maxHeight + navigationBarHeight
        val bottomSheetOffset = LocalBottomSheetOffset.current.value.roundToInt()

        offset.value = if (screenHeight / 2 <= bottomSheetOffset) {
            //snap to parent
            -parentHeight + screenHeight / 2 - navigationBarHeight
        } else {
            val visiblePart = screenHeight - bottomSheetOffset
            //fixed offset at bottom screen
            -parentHeight + visiblePart - navigationBarHeight
        }

        bottomBar()
    }
}
