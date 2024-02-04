package com.gnoemes.shimori.common.ui.resources.fonts

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

private val cache = mutableMapOf<String, Int>()

@SuppressLint("DiscouragedApi")
@Composable
actual fun font(
    name: String,
    resourceId: String,
    weight: FontWeight,
    style: FontStyle
): Font {
    val context = LocalContext.current
    val id = cache.getOrPut(resourceId) {
        context.resources.getIdentifier(resourceId, "font", context.packageName)
    }
    return Font(resId = id, weight = weight, style = style)
}