import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.LocalShimoriRateUtil
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.noRippleClickable
import com.gnoemes.shimori.common.ui.theme.ShimoriDefaultRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.lists.edit.ListEditInputState
import com.gnoemes.shimori.lists.edit.ListsEditViewModel
import com.gnoemes.shimori.lists.edit.R
import com.gnoemes.shimori.lists.edit.UiEvents
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ListsEdit(
    bottomSheetOffset: State<Float>,
    navigateUp: () -> Unit
) {
    ListsEdit(
        viewModel = shimoriViewModel(),
        bottomSheetOffset = bottomSheetOffset,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
private fun ListsEdit(
    viewModel: ListsEditViewModel,
    bottomSheetOffset: State<Float>,
    navigateUp: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect {
            when (it) {
                UiEvents.NavigateUp -> navigateUp()
            }
        }
    }

    ListEdit(
        bottomSheetOffset = bottomSheetOffset,
        inputState = state.inputState,
        titleName = with(LocalShimoriTextCreator.current) {
            state.title?.let(::name).orEmpty()
        },
        image = state.title?.image,
        status = state.status,
        anons = state.title?.status == TitleStatus.ANONS,
        progress = state.progress,
        size = state.title?.size,
        rewatches = state.rewatches,
        score = state.score,
        comment = state.comment,
        type = state.type,
        newRate = state.newRate,
        pinned = state.pinned,
        onStatusChanged = viewModel::onStatusChanged,
        onProgressChanged = viewModel::onProgressChanged,
        onRewatchesChanged = viewModel::onRewatchesChanged,
        onScoreChanged = viewModel::onScoreChanged,
        onProgressEdit = viewModel::onProgressInput,
        onRewatchesEdit = viewModel::onRewatchingInput,
        onCommentEdit = viewModel::onCommentInput,
        onCommentChanged = viewModel::onCommentChanged,
        onDefaultInputState = viewModel::onNoneInput,
        onDelete = viewModel::delete,
        onSave = viewModel::createOrUpdate,
        onTogglePin = viewModel::togglePin,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ListEdit(
    bottomSheetOffset: State<Float>,
    inputState: ListEditInputState,
    titleName: String,
    image: ShimoriImage?,
    status: RateStatus,
    anons: Boolean,
    progress: Int,
    size: Int?,
    rewatches: Int,
    score: Int?,
    comment: String?,
    type: RateTargetType,
    newRate: Boolean,
    pinned: Boolean,
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
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
        offset = offset,
        bottomSheetOffset = bottomSheetOffset,
        bottomBar = {
            if (inputState == ListEditInputState.None) {
                BottomBar(
                    modifier = Modifier
                        .height(84.dp)
                        .align(Alignment.BottomCenter)
                        .offset { IntOffset(0, offset.value) },
                    newRate = newRate,
                    pinned = pinned,
                    onDelete = onDelete,
                    onSave = onSave,
                    onTogglePin = onTogglePin
                )
            }
        }
    ) {
        Column {
            BottomSheetThumb()

            val title = when (inputState) {
                ListEditInputState.Progress -> stringResource(id = R.string.progress)
                ListEditInputState.Rewatching -> stringResource(id = R.string.re_watches)
                ListEditInputState.Comment -> stringResource(id = R.string.note)
                else -> titleName
            }

            var progressState by remember(progress) { mutableStateOf(progress) }

            val progressText = when {
                inputState != ListEditInputState.Progress -> null
                progressState == size -> null
                else -> stringResource(
                    id = R.string.left_format,
                    size?.let { it - progressState } ?: "?"
                )
            }

            Title(
                image = image,
                text = title,
                progressText = progressText
            )

            Spacer(modifier = Modifier.height(24.dp))

//            AnimatedContent(inputState) { state ->
            when (inputState) {
                ListEditInputState.Progress -> ProgressInputState(
                    progress = progressState,
                    progressDefault = progress,
                    size = size,
                    onProgressChanged = onProgressChanged,
                    onDefaultInputState = onDefaultInputState,
                    onChangedLocal = { progressState = it }
                )
                ListEditInputState.Rewatching -> RewatchingInputState(
                    rewatches = rewatches,
                    onRewatchesChanged = onRewatchesChanged,
                    onDefaultInputState = onDefaultInputState
                )
                ListEditInputState.Comment -> CommentInputState(
                    comment = comment,
                    onCommentChanged = onCommentChanged,
                    onDefaultInputState = onDefaultInputState
                )
                else -> DefaultInputState(
                    titleName = titleName,
                    type = type,
                    status = status,
                    anons = anons,
                    progress = progress,
                    size = size,
                    rewatches = rewatches,
                    score = score,
                    comment = comment,
                    onStatusChanged = onStatusChanged,
                    onProgressEdit = onProgressEdit,
                    onProgressChanged = onProgressChanged,
                    onRewatchesEdit = onRewatchesEdit,
                    onRewatchesChanged = onRewatchesChanged,
                    onScoreChanged = onScoreChanged,
                    onCommentEdit = onCommentEdit,
                )
            }
//            }
        }
    }
}

@Composable
private fun DefaultInputState(
    titleName: String,
    type: RateTargetType,
    status: RateStatus,
    anons: Boolean,
    progress: Int,
    size: Int?,
    rewatches: Int,
    score: Int?,
    comment: String?,
    onStatusChanged: (RateStatus) -> Unit,
    onProgressEdit: () -> Unit,
    onProgressChanged: (Int) -> Unit,
    onRewatchesEdit: () -> Unit,
    onRewatchesChanged: (Int) -> Unit,
    onScoreChanged: (Int?) -> Unit,
    onCommentEdit: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        StatusSelector(
            initialized = titleName.isNotEmpty(),
            type = type,
            selectedStatus = status,
            onStatusChanged = onStatusChanged
        )

        ProgressBoxes(
            anons = anons,
            progress = progress,
            size = size,
            onProgressChanged = onProgressChanged,
            onProgressEdit = onProgressEdit,
            rewatches = rewatches,
            onRewatchesChanged = onRewatchesChanged,
            onRewatchesEdit = onRewatchesEdit
        )

        Rating(
            score = score,
            onScoreChanged = onScoreChanged
        )

        Note(
            comment = comment,
            onCommentEdit = onCommentEdit
        )

        Spacer(
            modifier = Modifier
                .height(36.dp)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun ProgressInputState(
    progress: Int,
    progressDefault: Int,
    size: Int?,
    onProgressChanged: (Int) -> Unit,
    onDefaultInputState: () -> Unit,
    onChangedLocal: (Int) -> Unit,
) {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        ProgressInput(
            progress = progress,
            size = size,
            onChangedLocal = onChangedLocal,
            onCommit = { onProgressChanged(progress) }
        )

        EditButtons(
            onDefaultInputState = {
                onDefaultInputState()
                onChangedLocal(progressDefault)
            },
            onChangeAccept = {
                onProgressChanged(progress)
                onDefaultInputState()
            },
        )
    }
}

@Composable
private fun RewatchingInputState(
    rewatches: Int,
    onRewatchesChanged: (Int) -> Unit,
    onDefaultInputState: () -> Unit,
) {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        var rewatchesState by remember(rewatches) { mutableStateOf(rewatches) }

        RewatchesInput(
            rewatches = rewatchesState,
            onChangedLocal = { rewatchesState = it },
            onCommit = { onRewatchesChanged(rewatchesState) }
        )

        EditButtons(
            onDefaultInputState = onDefaultInputState,
            onChangeAccept = {
                onRewatchesChanged(rewatchesState)
                onDefaultInputState()
            },
        )
    }
}

@Composable
private fun CommentInputState(
    comment: String?,
    onCommentChanged: (String?) -> Unit,
    onDefaultInputState: () -> Unit
) {
    Column(
        Modifier.padding(horizontal = 16.dp)
    ) {
        var commentState by remember(comment) { mutableStateOf(comment) }

        CommentInput(
            commentState,
            onChangedLocal = { commentState = it },
            onCommit = { onCommentChanged(commentState) }
        )

        EditButtons(
            onDefaultInputState = onDefaultInputState,
            onChangeAccept = {
                onCommentChanged(commentState)
                onDefaultInputState()
            },
        )
    }
}

@Composable
private fun Title(
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
        Spacer(modifier = Modifier.width(8.dp))

        statuses.forEach { status ->
            ShimoriChip(
                onClick = { onStatusChanged(status) },
                text = LocalShimoriTextCreator.current.rateStatusText(type, status),
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
                        painter = painterResource(LocalShimoriRateUtil.current.rateStatusIcon(status)),
                        contentDescription = null
                    )
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun ProgressBoxes(
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
private fun Progress(
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
private fun Rewatches(
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
private fun Rating(
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
private fun ProgressBox(
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
private fun RowScope.ValueWithIncrementDecrementButtons(
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
                enabled = !newRate
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null
                )
            }

            ShimoriConformationButton(
                onClick = onSave,
                text = stringResource(id = if (newRate) R.string.add else R.string.save),
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

@Composable
private fun Note(
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
        textStyle = MaterialTheme.typography.labelLarge,
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
        cursorColor = MaterialTheme.colorScheme.primary,
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
        textStyle = MaterialTheme.typography.labelLarge,
        onValueChange = onChangedLocal,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 10.dp, max = 150.dp)
            .focusRequester(focusRequester),
        keyboardActions = KeyboardActions(onDone = { onCommit() }),
        hint = {
            Text(
                text = stringResource(id = R.string.add_note),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
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
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            IconButton(
                onClick = onDefaultInputState,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.weight(1f))


            IconButton(
                onClick = onChangeAccept,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_completed),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun SheetLayout(
    modifier: Modifier,
    offset: MutableState<Int>,
    bottomSheetOffset: State<Float>,
    bottomBar: @Composable BoxWithConstraintsScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    var parentHeight by remember { mutableStateOf(0) }
    val navigationBarHeight = WindowInsets.navigationBars.getBottom(LocalDensity.current)


    BoxWithConstraints(
        modifier = modifier then Modifier
            .onGloballyPositioned {
                parentHeight = it.size.height
            }
    ) {

        content()

        val screenHeight = constraints.maxHeight + navigationBarHeight
        val bottomSheetOffsetValue = bottomSheetOffset.value.roundToInt()

        offset.value = if (screenHeight / 2 <= bottomSheetOffsetValue) {
            //snap to parent
            -parentHeight + screenHeight / 2 - navigationBarHeight
        } else {
            val visiblePart = screenHeight - bottomSheetOffsetValue
            //fixed offset at bottom screen
            -parentHeight + visiblePart - navigationBarHeight
        }

        bottomBar()
    }
}
