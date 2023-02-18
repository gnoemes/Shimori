package com.gnoemes.shimori.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.LocalShimoriDimensions

class Dimensions(
    val listPosterHeight: Dp,
    val listPosterWidth: Dp,
    val bottomBarHeight: Dp,
    val bottomBarContainerHeight: Dp,
    val titlePosterHeight: Dp,
    val characterPosterWidth: Dp,
    val trailerPosterWidth: Dp,
    val trailerPosterHeight: Dp,
)


val defaultDimensions = Dimensions(
    listPosterHeight = 128.dp,
    listPosterWidth = 96.dp,
    bottomBarHeight = 80.dp,
    bottomBarContainerHeight = 88.dp,
    titlePosterHeight = 370.dp,
    characterPosterWidth = 120.dp,
    trailerPosterWidth = 328.dp,
    trailerPosterHeight = 164.dp,
)

val sw360Dimensions = Dimensions(
    listPosterHeight = 144.dp,
    listPosterWidth = 108.dp,
    bottomBarHeight = 80.dp,
    bottomBarContainerHeight = 88.dp,
    titlePosterHeight = 370.dp,
    characterPosterWidth = 120.dp,
    trailerPosterWidth = 328.dp,
    trailerPosterHeight = 164.dp,
)

val MaterialTheme.dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalShimoriDimensions.current