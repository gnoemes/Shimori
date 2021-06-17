package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.gnoemes.shimori.common.R


private val Manrope = FontFamily(
        Font(R.font.manrope_400, FontWeight.Normal),
        Font(R.font.manrope_600, FontWeight.SemiBold),
        Font(R.font.manrope_700, FontWeight.Bold),
)

private val h3 = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
private val h2 = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
private val subHead = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
private val keyInfo = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold)
private val subInfo = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
private val caption = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal)

val ShimoriTypography = Typography(
        defaultFontFamily = Manrope,
        h3 = h3,
        h2 = h2,
        subtitle1 = subHead,
        body1 = keyInfo,
        body2 = subInfo,
        caption = caption
)

val Typography.subHeadStyle get() = subHead
val Typography.keyInfoStyle get() = keyInfo
val Typography.subInfoStyle get() = subInfo