package com.gnoemes.shimori.common.ui.overlays

import androidx.compose.runtime.staticCompositionLocalOf
import com.slack.circuit.runtime.Navigator

val LocalNavigator = staticCompositionLocalOf<Navigator> { Navigator.NoOp }