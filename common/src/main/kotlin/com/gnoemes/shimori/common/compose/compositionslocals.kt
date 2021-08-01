package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.staticCompositionLocalOf
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.common.utils.ShimoriRateUtil
import com.gnoemes.shimori.common.utils.ShimoriTextCreator

val LocalShimoriRateUtil = staticCompositionLocalOf<ShimoriRateUtil> { error("empty") }
val LocalShimoriTextCreator = staticCompositionLocalOf<ShimoriTextCreator> { error("empty") }
val LocalShimoriSettings = staticCompositionLocalOf<ShimoriPreferences> { error("empty") }