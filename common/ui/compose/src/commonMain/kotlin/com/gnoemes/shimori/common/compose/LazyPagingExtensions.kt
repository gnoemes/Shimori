@file:Suppress("NOTHING_TO_INLINE")

package com.gnoemes.shimori.common.compose

import androidx.compose.runtime.Composable
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.cash.paging.LoadStateError
import com.gnoemes.shimori.common.compose.ui.UiMessage
import com.slack.circuit.retained.rememberRetained
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow


inline fun CombinedLoadStates.appendErrorOrNull(): UiMessage? {
    return (append as? LoadStateError)?.let { UiMessage(it.error) }
}

inline fun CombinedLoadStates.prependErrorOrNull(): UiMessage? {
    return (prepend as? LoadStateError)?.let { UiMessage(it.error) }
}

inline fun CombinedLoadStates.refreshErrorOrNull(): UiMessage? {
    return (refresh as? LoadStateError)?.let { UiMessage(it.error) }
}

@Composable
inline fun <T : Any> Flow<PagingData<T>>.rememberRetainedCachedPagingFlow(
    scope: CoroutineScope = rememberRetainedCoroutineScope(),
): Flow<PagingData<T>> = rememberRetained(this, scope) { cachedIn(scope) }