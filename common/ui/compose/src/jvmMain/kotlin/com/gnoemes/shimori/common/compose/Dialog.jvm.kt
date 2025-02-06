package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
            usePlatformInsets = false,
            scrimColor = Color.Transparent
        ),
        content = content
    )
}