package com.gnoemes.shimori.common.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.compose.LocalShimoriDimensions

class Dimensions(
    val listPosterHeight: Dp,
    val listPosterWidth: Dp
)


val defaultDimensions = Dimensions(
        listPosterHeight = 128.dp,
        listPosterWidth = 96.dp
)

val sw360Dimensions = Dimensions(
        listPosterHeight = 144.dp,
        listPosterWidth = 108.dp
)

val MaterialTheme.dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalShimoriDimensions.current