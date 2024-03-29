package com.gnoemes.shimori.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformParagraphStyle
import androidx.compose.ui.text.PlatformSpanStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gnoemes.shimori.ui.R

private val Manrope = FontFamily(
    Font(R.font.manrope_400, FontWeight.Normal),
    Font(R.font.manrope_500, FontWeight.Medium),
)

private val platformStyleDefault =
    PlatformTextStyle(PlatformSpanStyle.Default, PlatformParagraphStyle(false))

private val displayLarge = TextStyle(
    fontSize = 57.sp,
    lineHeight = 64.sp,
    letterSpacing = (-0.25).sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val displayMedium = TextStyle(
    fontSize = 45.sp,
    lineHeight = 52.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val displaySmall = TextStyle(
    fontSize = 36.sp,
    lineHeight = 44.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val headlineLarge = TextStyle(
    fontSize = 32.sp,
    lineHeight = 40.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val headlineMedium = TextStyle(
    fontSize = 28.sp,
    lineHeight = 36.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val headlineSmall = TextStyle(
    fontSize = 24.sp,
    lineHeight = 32.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val titleLarge = TextStyle(
    fontSize = 22.sp,
    lineHeight = 28.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val titleMedium = TextStyle(
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.1.sp,
    fontWeight = FontWeight.Medium,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val titleSmall = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp,
    fontWeight = FontWeight.Medium,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val bodyLarge = TextStyle(
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val bodyMedium = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val bodySmall = TextStyle(
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val labelLarge = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp,
    fontWeight = FontWeight.Medium,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val labelMedium = TextStyle(
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp,
    fontWeight = FontWeight.Medium,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)
private val labelSmall = TextStyle(
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp,
    fontWeight = FontWeight.Medium,
    fontStyle = FontStyle.Normal,
    fontFamily = Manrope,
    platformStyle = platformStyleDefault,
)

val ShimoriTypography = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall
)