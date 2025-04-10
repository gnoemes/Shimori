package com.gnoemes.shimori.common.compose.theme

import androidx.compose.runtime.Composable
import com.gnoemes.shimori.common.compose.LocalIsDarkColors
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_no_image_dark
import com.gnoemes.shimori.common.ui.resources.icons.ic_no_image_light


val Icons.ic_no_image
    @Composable
    get() = if (LocalIsDarkColors.current) ic_no_image_dark else ic_no_image_light