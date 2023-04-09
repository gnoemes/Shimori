package com.gnoemes.shimori.lists.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.components.ShimoriTextField
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.lists.edit.R

private val numberRegex = "(?![0-9]{1,4})".toRegex()

@Composable
internal fun DefaultInputState(
    titleName: String,
    type: TrackTargetType,
    status: TrackStatus,
    anons: Boolean,
    progress: Int,
    size: Int?,
    rewatches: Int,
    score: Int?,
    comment: String?,
    onStatusChanged: (TrackStatus) -> Unit,
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
internal fun ProgressInputState(
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
internal fun RewatchingInputState(
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
internal fun CommentInputState(
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