package com.gnoemes.shimori.lists.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.gnoemes.shimori.common.ui.components.ScaffoldExtended

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenLayout(
    content: @Composable (PaddingValues) -> Unit
) {
    ScaffoldExtended(
        content = content
    )
}