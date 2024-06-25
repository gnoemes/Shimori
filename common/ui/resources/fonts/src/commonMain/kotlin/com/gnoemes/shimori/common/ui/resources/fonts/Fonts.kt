package com.gnoemes.shimori.common.ui.resources.fonts

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import shimori.common.ui.resources.fonts.generated.resources.Res
import shimori.common.ui.resources.fonts.generated.resources.manrope_400
import shimori.common.ui.resources.fonts.generated.resources.manrope_500

val ManropeFamily: FontFamily
    @Composable get() = FontFamily(
        org.jetbrains.compose.resources.Font(Res.font.manrope_400, FontWeight.Normal),
        org.jetbrains.compose.resources.Font(Res.font.manrope_500, FontWeight.Medium),
    )