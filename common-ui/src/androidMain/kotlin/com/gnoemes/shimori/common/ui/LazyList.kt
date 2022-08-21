package com.gnoemes.shimori.common.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.gnoemes.shimori.common.ui.api.UiMessage

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

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListState(): LazyListState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info: https://issuetracker.google.com/issues/177245496.
    return when (itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) { LazyListState(0, 0) }
        // Return rememberLazyListState (normal case).
        else -> androidx.compose.foundation.lazy.rememberLazyListState()
    }
}