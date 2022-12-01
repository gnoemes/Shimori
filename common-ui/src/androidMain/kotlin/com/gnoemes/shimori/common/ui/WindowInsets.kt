package com.gnoemes.shimori.common.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable

val WindowInsets.Companion.empty
    @Composable
    @NonRestartableComposable
    get() = WindowInsets(0, 0, 0, 0)