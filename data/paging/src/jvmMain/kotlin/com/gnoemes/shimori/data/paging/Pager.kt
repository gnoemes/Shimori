package com.gnoemes.shimori.data.paging

import androidx.paging.Pager
import kotlinx.coroutines.flow.Flow

actual class Pager<K : Any, V : Any> actual constructor(
    config: PagingConfig,
    initialKey: K?,
    pagingSourceFactory: () -> PagingSource<K, V>
) {
    actual val pagingData: Flow<PagingData<V>> = Pager(
        config = config,
        initialKey = initialKey,
        pagingSourceFactory = pagingSourceFactory
    ).flow
}