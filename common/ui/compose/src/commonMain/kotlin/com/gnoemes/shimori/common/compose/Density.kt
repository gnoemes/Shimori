package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density

@Composable
fun Density.isKeyboardVisible(): Boolean {
    return WindowInsets.Companion.ime.getBottom(this) > 0
}