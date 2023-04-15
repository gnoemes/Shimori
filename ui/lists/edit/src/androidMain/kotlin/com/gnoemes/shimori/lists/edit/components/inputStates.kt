package com.gnoemes.shimori.lists.edit.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.components.ShimoriTextField
import com.gnoemes.shimori.lists.edit.R

private val numberRegex = "(?![0-9]{1,4})".toRegex()

@Composable
internal fun ColumnScope.ProgressInput(
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
internal fun ColumnScope.RewatchesInput(
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
private fun ColumnScope.NumberInput(
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
        textStyle = MaterialTheme.typography.titleMedium,
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
            .weight(1f)
            .focusRequester(focusRequester),
        cursorColor = MaterialTheme.colorScheme.primary,
        keyboardActions = KeyboardActions(onDone = { onCommit() }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
    )
}


@Composable
internal fun CommentInput(
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        hint = {
            Text(
                text = stringResource(id = R.string.add_note),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
            )
        }
    )
}