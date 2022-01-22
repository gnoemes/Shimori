package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.staticCompositionLocalOf
import com.gnoemes.shimori.base.entities.ShikimoriAuthState
import com.gnoemes.shimori.base.settings.ShimoriSettings
import com.gnoemes.shimori.common.compose.theme.Dimensions
import com.gnoemes.shimori.common.utils.ShimoriRateUtil
import com.gnoemes.shimori.common.utils.ShimoriTextCreator

val LocalShimoriRateUtil = staticCompositionLocalOf<ShimoriRateUtil> { error("empty") }
val LocalShimoriTextCreator = staticCompositionLocalOf<ShimoriTextCreator> { error("empty") }
val LocalShimoriSettings = staticCompositionLocalOf<ShimoriSettings> { error("empty") }
val LocalShimoriDimensions = staticCompositionLocalOf<Dimensions> { error("empty") }
val LocalShikimoriAuth = staticCompositionLocalOf<ShikimoriAuthState> { error("empty") }