package com.gnoemes.shimori.tracks.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalShimoriIconsUtil
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.ProgressNumberVisualTransformation
import com.gnoemes.shimori.common.compose.itemSpacer
import com.gnoemes.shimori.common.compose.mouseWheelNestedScrollConnectionFix
import com.gnoemes.shimori.common.compose.ui.RatingBar
import com.gnoemes.shimori.common.compose.ui.StepSize
import com.gnoemes.shimori.common.compose.ui.gradientBackground
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_back
import com.gnoemes.shimori.common.ui.resources.icons.ic_delete
import com.gnoemes.shimori.common.ui.resources.strings.add
import com.gnoemes.shimori.common.ui.resources.strings.edit
import com.gnoemes.shimori.common.ui.resources.strings.note
import com.gnoemes.shimori.common.ui.resources.strings.progress
import com.gnoemes.shimori.common.ui.resources.strings.re_watches
import com.gnoemes.shimori.common.ui.resources.strings.save
import com.gnoemes.shimori.common.ui.resources.strings.title_note_hint
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.screens.TrackEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@CircuitInject(screen = TrackEditScreen::class, scope = UiScope::class)
internal fun TrackEditUi(
    state: TrackEditUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    TrackEditUi(
        isEdit = state.isEdit,
        titleName = state.titleName,
        type = state.type,
        selectedStatus = state.status,
        score = state.score,
        progress = state.progress,
        maxProgress = state.maxProgress,
        repeats = state.repeats,
        decrementEnabled = state.decrementEnabled,
        incrementEnabled = state.incrementEnabled,
        note = state.note,
        changeStatus = { eventSink(TrackEditUiEvent.ChangeStatus(it)) },
        changeScore = { eventSink(TrackEditUiEvent.ChangeScore(it)) },
        changeProgress = { eventSink(TrackEditUiEvent.ChangeProgress(it)) },
        changeRepeats = { eventSink(TrackEditUiEvent.ChangeRepeats(it)) },
        decrementProgress = { eventSink(TrackEditUiEvent.DecrementProgress) },
        incrementProgress = { eventSink(TrackEditUiEvent.IncrementProgress) },
        changeNote = { eventSink(TrackEditUiEvent.ChangeNote(it)) },
        delete = { eventSink(TrackEditUiEvent.Delete) },
        save = { eventSink(TrackEditUiEvent.Save) },
        navigateUp = { eventSink(TrackEditUiEvent.NavigateUp) }
    )
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun TrackEditUi(
    isEdit: Boolean,
    titleName: String,
    type: TrackTargetType,
    selectedStatus: TrackStatus,
    score: Int?,
    progress: Int,
    maxProgress: Int,
    repeats: Int,
    decrementEnabled: Boolean,
    incrementEnabled: Boolean,
    note: String?,
    changeStatus: (TrackStatus) -> Unit,
    changeScore: (Int) -> Unit,
    changeProgress: (Int) -> Unit,
    changeRepeats: (Int) -> Unit,
    decrementProgress: () -> Unit,
    incrementProgress: () -> Unit,
    changeNote: (String?) -> Unit,
    delete: () -> Unit,
    save: () -> Unit,
    navigateUp: () -> Unit,
) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val scrollBehavior by remember(behavior) { mutableStateOf(behavior) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val isCollapsed by remember(scrollBehavior.state) { derivedStateOf { scrollBehavior.state.collapsedFraction == 1f } }

            val animateColor by animateColorAsState(
                when {
                    scrollBehavior.state.collapsedFraction <= 0.1f -> Color.Transparent
                    else -> MaterialTheme.colorScheme.surface.copy(alpha = scrollBehavior.state.collapsedFraction)
                }
            )

            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors().copy(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = animateColor,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                expandedHeight = 148.dp,
                scrollBehavior = scrollBehavior,
                title = {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .animateContentSize(),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            titleName,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = if (isCollapsed) 1 else 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (isCollapsed) {
                            Text(
                                stringResource(if (isEdit) Strings.edit else Strings.add),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(navigateUp) {
                        Icon(painterResource(Icons.ic_back), contentDescription = null)
                    }
                },
                actions = {
                    if (isEdit) {
                        IconButton(delete) {
                            Icon(painterResource(Icons.ic_delete), contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { paddingValue ->
        TrackEditUiContent(
            paddingValue = paddingValue,
            scrollBehavior = scrollBehavior,
            isEdit = isEdit,
            type = type,
            selectedStatus = selectedStatus,
            score = score,
            progress = progress,
            maxProgress = maxProgress,
            repeats = repeats,
            decrementEnabled = decrementEnabled,
            incrementEnabled = incrementEnabled,
            note = note,
            changeStatus = changeStatus,
            changeScore = changeScore,
            changeProgress = changeProgress,
            changeRepeats = changeRepeats,
            decrementProgress = decrementProgress,
            incrementProgress = incrementProgress,
            changeNote = changeNote,
            save = save
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun TrackEditUiContent(
    paddingValue: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    isEdit: Boolean,
    type: TrackTargetType,
    selectedStatus: TrackStatus,
    score: Int?,
    progress: Int,
    maxProgress: Int,
    repeats: Int,
    decrementEnabled: Boolean,
    incrementEnabled: Boolean,
    note: String?,
    changeStatus: (TrackStatus) -> Unit,
    changeScore: (Int) -> Unit,
    changeProgress: (Int) -> Unit,
    changeRepeats: (Int) -> Unit,
    decrementProgress: () -> Unit,
    incrementProgress: () -> Unit,
    changeNote: (String?) -> Unit,
    save: () -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current
    val icons = LocalShimoriIconsUtil.current
    val density = LocalDensity.current
    var parentWidth by remember { mutableStateOf(0.dp) }

    val state = rememberLazyListState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(.66f)
                .gradientBackground()
                .align(Alignment.TopCenter)
        )
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            LazyColumn(
                state = state,
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .mouseWheelNestedScrollConnectionFix(state, scrollBehavior)
                    .onSizeChanged { size ->
                        parentWidth = with(density) {
                            size.width.toDp()
                        }
                    }
                    .imePadding()
            ) {

                itemSpacer(paddingValue.calculateTopPadding(), "gradient_spacer")


                item("statuses") {
                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentSize provides 0.dp
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TrackStatus.listPagesOrder.forEach { status ->
                                val isSelected = selectedStatus == status
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { changeStatus(status) },
                                    label = {
                                        Text(
                                            textCreator {
                                                status.name(type)
                                            },
                                        )
                                    },
                                    leadingIcon = {
                                        AnimatedVisibility(isSelected) {
                                            Icon(
                                                painterResource(icons.trackStatusIcon(status)),
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                itemSpacer(24.dp)

                item("score") {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val scoreFloat = score?.div(2f) ?: 0f
                        val starSize = remember { 40.dp }
                        var localScore by remember(score) { mutableStateOf(scoreFloat) }
                        val spaceBetween by remember(parentWidth) {
                            derivedStateOf {
                                ((parentWidth - starSize * 5) / 4).coerceIn(8.dp, 40.dp)
                            }
                        }

                        RatingBar(
                            value = localScore,
                            initialValue = scoreFloat,
                            stepSize = StepSize.HALF,
                            spaceBetween = spaceBetween,
                            size = starSize,
                            modifier = Modifier.align(Alignment.Center),
                            onValueChange = { localScore = it },
                            onRatingChanged = { changeScore((it * 2).toInt()) }
                        )
                    }
                }

                itemSpacer(24.dp)

                item("progress_and_repeats") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val transformation =
                            remember(maxProgress) { ProgressNumberVisualTransformation(maxProgress) }
                        var progressState by remember { mutableStateOf(TextFieldValue()) }
                        OutlinedTextField(
                            TextFieldValue(
                                text = "$progress",
                                selection = TextRange("$progress".length)
                            ),
                            onValueChange = { newValue ->
                                progressState = newValue
                                val newProgress = transformation.getProgress(newValue.text)
                                changeProgress(newProgress)
                            },
                            label = { Text(stringResource(Strings.progress)) },
                            modifier = Modifier.weight(.63f),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge,
                            visualTransformation = transformation,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number,
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                val newProgress = transformation.getProgress(progressState.text)
                                changeProgress(newProgress)
                            })
                        )

                        var repeatsState by remember { mutableStateOf(TextFieldValue()) }
                        OutlinedTextField(
                            TextFieldValue(
                                text = "$repeats",
                            ),
                            onValueChange = { newValue ->
                                repeatsState = newValue
                                val newRepeats = newValue.text.filter { it.isDigit() }.toInt()
                                changeRepeats(newRepeats)
                            },
                            label = { Text(stringResource(Strings.re_watches)) },
                            modifier = Modifier.weight(.36f),
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyLarge,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number,
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                val newRepeats = repeatsState.text.filter { it.isDigit() }.toInt()
                                changeRepeats(newRepeats)
                            })
                        )
                    }
                }

                itemSpacer(16.dp)

                item("increment_decrement") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledTonalButton(
                            onClick = decrementProgress,
                            modifier = Modifier.weight(1f),
                            enabled = decrementEnabled,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("-1")
                        }

                        FilledTonalButton(
                            onClick = incrementProgress,
                            modifier = Modifier.weight(1f),
                            enabled = incrementEnabled,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("+1")
                        }
                    }
                }

                itemSpacer(16.dp)

                item("note") {
                    var noteState by remember { mutableStateOf("") }
                    OutlinedTextField(
                        note ?: "",
                        onValueChange = { newValue ->
                            noteState = newValue
                            changeNote(newValue)
                        },
                        label = { Text(stringResource(Strings.note)) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text,
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            val newNote = noteState
                            changeNote(newNote)
                        })
                    )
                }

                itemSpacer(8.dp)

                item("note_hint") {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            stringResource(Strings.title_note_hint),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                itemSpacer(88.dp)
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth()
                .background(LocalAbsoluteTonalElevation.current.let {
                    MaterialTheme.colorScheme.surfaceColorAtElevation(it).copy(alpha = 0.33f)
                })
                .align(Alignment.BottomCenter)
                .zIndex(1f)
                .padding(20.dp)
        ) {
            androidx.compose.material3.Button(
                onClick = save,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    stringResource(
                        if (isEdit) Strings.edit
                        else Strings.save
                    )
                )
            }
        }
    }


}

