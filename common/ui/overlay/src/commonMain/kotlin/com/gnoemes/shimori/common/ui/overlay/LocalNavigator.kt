package com.gnoemes.shimori.common.ui.overlay

import androidx.compose.runtime.staticCompositionLocalOf
import com.slack.circuit.runtime.Navigator

val LocalNavigator = staticCompositionLocalOf<Navigator> { Navigator.NoOp }