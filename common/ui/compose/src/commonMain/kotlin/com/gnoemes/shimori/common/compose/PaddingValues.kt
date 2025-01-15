package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun PaddingValues.copy(
    copyStart: Boolean = true,
    copyTop: Boolean = true,
    copyEnd: Boolean = true,
    copyBottom: Boolean = true
): PaddingValues {
    return remember(this) {
        derivedStateOf {
            PaddingValues(
                start = if (copyStart) calculateStartPadding(LayoutDirection.Ltr) else 0.dp,
                top = if (copyTop) calculateTopPadding() else 0.dp,
                end = if (copyEnd) calculateEndPadding(LayoutDirection.Ltr) else 0.dp,
                bottom = if (copyBottom) calculateBottomPadding() else 0.dp
            )
        }
    }.value
}

fun PaddingValues.plus(
    plus: PaddingValues,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): PaddingValues = PaddingValues(
    start = calculateStartPadding(layoutDirection) + plus.calculateStartPadding(layoutDirection),
    top = calculateTopPadding() + plus.calculateTopPadding(),
    end = calculateEndPadding(layoutDirection) + plus.calculateEndPadding(layoutDirection),
    bottom = calculateBottomPadding() + plus.calculateBottomPadding(),
)

fun PaddingValues.minus(
    other: PaddingValues,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): PaddingValues = PaddingValues(
    start = (calculateStartPadding(layoutDirection) - other.calculateStartPadding(layoutDirection)).coerceAtLeast(0.dp),
    top = (calculateTopPadding() - other.calculateTopPadding()).coerceAtLeast(0.dp),
    end = (calculateEndPadding(layoutDirection) - other.calculateEndPadding(layoutDirection)).coerceAtLeast(0.dp),
    bottom = (calculateBottomPadding() - other.calculateBottomPadding()).coerceAtLeast(0.dp),
)