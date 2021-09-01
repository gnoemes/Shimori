package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.compose.theme.caption

@Composable
fun ColumnScope.BottomSheetThumb(withSpace: Boolean = true) {
    val thumbColor = MaterialTheme.colors.caption
    Canvas(modifier = Modifier
        .padding(vertical = 4.dp)
        .height(4.dp)
        .width(16.dp)
        .align(Alignment.CenterHorizontally)
    ) {

        drawRoundRect(
                color = thumbColor,
                cornerRadius = CornerRadius(x = 16f, y = 16f)
        )
    }

    if (withSpace) {
        Spacer(modifier = Modifier.height(16.dp))
    }
}