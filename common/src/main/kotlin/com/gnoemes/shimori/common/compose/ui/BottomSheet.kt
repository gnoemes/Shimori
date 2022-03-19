package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.BottomSheetTitle(
    text: String
) {
    BottomSheetThumb(withSpace = false)

    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun ColumnScope.BottomSheetThumb(withSpace: Boolean = true) {
    val thumbColor = MaterialTheme.colorScheme.onSurface
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