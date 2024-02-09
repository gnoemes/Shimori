package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.staticCompositionLocalOf
import com.gnoemes.shimori.settings.ShimoriSettings

//val LocalShimoriTrackUtil = staticCompositionLocalOf<ShimoriTrackUtil> { error("empty") }
//val LocalShimoriTextCreator = staticCompositionLocalOf<ShimoriTextCreator> { error("empty") }
val LocalShimoriSettings = staticCompositionLocalOf<ShimoriSettings> { error("empty") }