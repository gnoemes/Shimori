package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.staticCompositionLocalOf
import com.gnoemes.shimori.common.ui.resources.ShimoriIconsUtil
import com.gnoemes.shimori.common.ui.resources.util.ShimoriDateTextFormatter
import com.gnoemes.shimori.common.ui.resources.util.ShimoriTextCreator
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.settings.ShimoriSettings

val LocalShimoriIconsUtil = staticCompositionLocalOf<ShimoriIconsUtil> { error("empty") }
val LocalShimoriTextCreator = staticCompositionLocalOf<ShimoriTextCreator> { error("empty") }
val LocalShimoriDateTextFormatter = staticCompositionLocalOf<ShimoriDateTextFormatter> { error("empty") }
val LocalShimoriSettings = staticCompositionLocalOf<ShimoriSettings> { error("empty") }
val LocalShimoriPreferences = staticCompositionLocalOf<ShimoriPreferences> { error("empty") }