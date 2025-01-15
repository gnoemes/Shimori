package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimoriCard(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    color: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = 1.dp,
    gradientPainter: Painter? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        tonalElevation = tonalElevation,
    ) {
        Column(
            modifier = Modifier
                .gradientBackground(gradientPainter, shape)
        ) {
            if (title != null) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.titleSmall,
                ) {
                    Box(
                        Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    ) {
                        title()
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            content()

            Spacer(Modifier.height(16.dp))
        }

    }
}