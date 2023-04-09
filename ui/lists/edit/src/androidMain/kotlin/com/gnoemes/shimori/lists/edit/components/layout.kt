package com.gnoemes.shimori.lists.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.components.ConfirmationButtonType
import com.gnoemes.shimori.common.ui.components.ShimoriConformationButton
import com.gnoemes.shimori.lists.edit.R
import kotlin.math.roundToInt

@Composable
internal fun SheetLayout(
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


@Composable
internal fun EditButtons(
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