package com.gnoemes.shimori.common.compose.theme

import androidx.compose.ui.graphics.Color
import com.gnoemes.shimori.settings.AppAccentColor

internal fun seedColorFromType(type: AppAccentColor) = when (type) {
    AppAccentColor.Red -> accentRed
    AppAccentColor.Orange -> accentOrange
    AppAccentColor.Yellow -> accentYellow
    AppAccentColor.Green -> accentGreen
    AppAccentColor.Blue -> accentBlue
    AppAccentColor.Purple -> accentPurple
    else -> accentYellow
}

val disabledContainerAlpha = 0.12f
val disabledContentAlpha = 0.38f

val titleAnnounced = Color(0xFFFF2D58)
val titleOngoing = Color(0xFF0088FF)
val titlePaused = Color(0xFFa063cf)
val titleDiscontinued = Color(0xFFb19143)

val commentSummary = Color(0xFF4AAA4A)
val commentOfftop = Color(0xFFF58EBB)

val statsBlue = Color(0xFF2AA8F2)
val statsGreen = Color(0xFF8bd448)
val statsYellow = Color(0xFFfae442)
val statsPurple = Color(0xFF9c4f96)
val statsOrange = Color(0xFFfba949)
val statsRed = Color(0xFFff6355)

val accentRed = Color(0xFFef5350)
val accentOrange = Color(0xFFff9800)
val accentYellow = Color(0xFFeec234)
val accentGreen = Color(0xFF43a047)
val accentBlue = Color(0xFF1e88e5)
val accentPurple = Color(0xFF5e35b1)

val favorite = Color(0xFFF13737)
val favoriteContainer = Color(0xFF751717)

val lightError = Color(0xFFBA1B1B)
val lightOnError = Color(0xFFFFFFFF)
val lightErrorContainer = Color(0xFFFFDAD4)
val lightOnErrorContainer = Color(0xFF410001)

val darkError = Color(0xFFFFB4A9)
val darkOnError = Color(0xFF680003)
val darkErrorContainer = Color(0xFF930006)
val darkOnErrorContainer = Color(0xFFFFDAD4)