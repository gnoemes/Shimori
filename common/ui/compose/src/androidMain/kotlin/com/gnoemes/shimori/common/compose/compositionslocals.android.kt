package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.app.ComponentActivity

val LocalActivity = staticCompositionLocalOf<ComponentActivity> { error("empty") }