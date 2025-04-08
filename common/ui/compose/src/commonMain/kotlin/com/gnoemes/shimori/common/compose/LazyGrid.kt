@file:Suppress("NOTHING_TO_INLINE")

package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

inline fun LazyGridScope.itemSpacer(height: Dp, key: Any? = null) {
    item(
        span = { GridItemSpan(this.maxLineSpan) },
        key = key
    ) {
        Spacer(
            Modifier
                .height(height)
                .fillMaxWidth(),
        )
    }
}