package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.gnoemes.shimori.common.api.UiMessage

fun LazyListScope.itemSpacer(height: Dp) {
    item {
        Spacer(
                Modifier
                    .height(height)
                    .fillParentMaxWidth()
        )
    }
}

fun LazyListScope.itemSpacer(modifier: Modifier) {
    item {
        Spacer(
                modifier
                    .fillParentMaxWidth()
        )
    }
}

fun CombinedLoadStates.appendErrorOrNull(): UiMessage? {
    return (append.takeIf { it is LoadState.Error } as? LoadState.Error)
        ?.let { UiMessage(it.error) }
}

fun CombinedLoadStates.prependErrorOrNull(): UiMessage? {
    return (prepend.takeIf { it is LoadState.Error } as? LoadState.Error)
        ?.let { UiMessage(it.error) }
}

fun CombinedLoadStates.refreshErrorOrNull(): UiMessage? {
    return (refresh.takeIf { it is LoadState.Error } as? LoadState.Error)
        ?.let { UiMessage(it.error) }
}