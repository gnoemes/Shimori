package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.Composable

@Composable
expect fun Dialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
)