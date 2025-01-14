package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import com.gnoemes.shimori.common.ui.resources.fonts.ManropeFamily

val ShimoriTypography: Typography
    @Composable get() {
        val default = Typography()
        val fontFamily = ManropeFamily

        return Typography(
            displayLarge = default.displayLarge.copy(fontFamily = fontFamily),
            displayMedium = default.displayMedium.copy(fontFamily = fontFamily),
            displaySmall = default.displaySmall.copy(fontFamily = fontFamily),
            headlineLarge = default.headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = default.headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = default.headlineSmall.copy(fontFamily = fontFamily),
            titleLarge = default.titleLarge.copy(fontFamily = fontFamily),
            titleMedium = default.titleMedium.copy(fontFamily = fontFamily),
            titleSmall = default.titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = default.bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = default.bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = default.bodySmall.copy(fontFamily = fontFamily),
            labelLarge = default.labelLarge.copy(fontFamily = fontFamily),
            labelMedium = default.labelMedium.copy(fontFamily = fontFamily),
            labelSmall = default.labelSmall.copy(fontFamily = fontFamily),
        )
    }