package com.gnoemes.shimori.common.compose

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.gnoemes.shimori.common.R

@Composable
fun ChevronIcon(
    painter: Painter = painterResource(R.drawable.ic_chevron_right),
    tint: Color = MaterialTheme.colors.onPrimary
) {
    Icon(
            painter = painter,
            contentDescription = null,
            tint = tint
    )
}