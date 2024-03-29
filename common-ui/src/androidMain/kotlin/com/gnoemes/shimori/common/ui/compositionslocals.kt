package com.gnoemes.shimori.common.ui

import android.content.SharedPreferences
import androidx.compose.runtime.staticCompositionLocalOf
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.common.ui.theme.Dimensions
import com.gnoemes.shimori.common.ui.utils.ShimoriTextCreator
import com.gnoemes.shimori.common.ui.utils.ShimoriTrackUtil

val LocalShimoriTrackUtil = staticCompositionLocalOf<ShimoriTrackUtil> { error("empty") }
val LocalShimoriTextCreator = staticCompositionLocalOf<ShimoriTextCreator> { error("empty") }
val LocalShimoriSettings = staticCompositionLocalOf<ShimoriSettings> { error("empty") }
val LocalShimoriDimensions = staticCompositionLocalOf<Dimensions> { error("empty") }
val LocalPreferences = staticCompositionLocalOf<SharedPreferences> { error("empty") }