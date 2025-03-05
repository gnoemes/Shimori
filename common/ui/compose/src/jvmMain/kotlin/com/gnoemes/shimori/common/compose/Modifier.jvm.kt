package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent

//https://youtrack.jetbrains.com/issue/CMP-6015 workout
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
actual fun Modifier.mouseWheelNestedScrollConnectionFix(
    state: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
): Modifier {
    return this.onPointerEvent(PointerEventType.Scroll) { event ->
        val currentItem = state.layoutInfo.visibleItemsInfo[0].index
        val currentItemOffset = state.layoutInfo.visibleItemsInfo[0].offset
        val delta = event.changes.last().scrollDelta.y
        if (currentItem == 0 && currentItemOffset == 0 && delta < 0) {
            val toolbarOffset = scrollBehavior.state.heightOffset
            scrollBehavior.state.heightOffset = (toolbarOffset - delta * 4).coerceIn(
                minimumValue = toolbarOffset,
                maximumValue = 0f
            )
        }
    }
}