package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun Modifier.mouseWheelNestedScrollConnectionFix(
    state: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior
): Modifier = this