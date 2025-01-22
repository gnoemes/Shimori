@file:Suppress("NOTHING_TO_INLINE")

package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline fun LazyListScope.itemSpacer(height: Dp, key : Any? = null) {
    item(key) {
        Spacer(
            Modifier
                .height(height)
                .fillParentMaxWidth(),
        )
    }
}

inline fun LazyListScope.statusBarSpacer(additional: Dp = 0.dp) {
    item("status_bar_spacer") {
        Spacer(
            Modifier
                .statusBarHeight(additional)
                .fillParentMaxWidth(),
        )
    }
}