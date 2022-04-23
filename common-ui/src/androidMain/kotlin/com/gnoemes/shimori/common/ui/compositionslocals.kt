package com.gnoemes.shimori.common.ui

import androidx.compose.runtime.staticCompositionLocalOf
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.common.ui.theme.Dimensions
import com.gnoemes.shimori.common.ui.utils.ShimoriRateUtil

val LocalShimoriRateUtil = staticCompositionLocalOf<ShimoriRateUtil> { error("empty") }
//val LocalShimoriTextCreator = staticCompositionLocalOf<ShimoriTextCreator> { error("empty") }
val LocalShimoriSettings = staticCompositionLocalOf<ShimoriSettings> { error("empty") }
val LocalShimoriDimensions = staticCompositionLocalOf<Dimensions> { error("empty") }
//val LocalShikimoriAuth = staticCompositionLocalOf<ShikimoriAuthState> { error("empty") }