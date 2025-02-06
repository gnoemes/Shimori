package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

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
            dismissOnBackPress = true,
        ),
        content = content
    )
}