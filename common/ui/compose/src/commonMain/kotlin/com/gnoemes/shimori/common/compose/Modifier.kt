package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) { onClick() }
}

fun Modifier.statusBarHeight(): Modifier = composed {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    height(statusBarHeight)
}

fun Modifier.statusBarHeight(additional: Dp): Modifier = composed {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    height(statusBarHeight + additional)
}

fun Modifier.ignoreHorizontalParentPadding(horizontal: Dp): Modifier {
    return this.layout { measurable, constraints ->
        val overridenWidth = constraints.maxWidth + 2 * horizontal.roundToPx()
        val placeable = measurable.measure(constraints.copy(maxWidth = overridenWidth))
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

@Composable
fun Modifier.placeholder(
    visible: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
): Modifier {
    return placeholder(
        visible = visible,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        highlight = PlaceholderHighlight.shimmer(
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.52f)
        ),
        shape = shape,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun Modifier.mouseWheelNestedScrollConnectionFix(
    state: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
): Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun Modifier.mouseWheelNestedScrollConnectionFix(
    state: LazyGridState,
    scrollBehavior: TopAppBarScrollBehavior,
): Modifier