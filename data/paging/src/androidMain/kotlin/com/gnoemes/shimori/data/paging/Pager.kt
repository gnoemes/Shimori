package com.gnoemes.shimori.data.paging

import kotlinx.coroutines.flow.Flow
import androidx.paging.Pager as AndroidPager

actual class Pager<K : Any, V : Any> actual constructor(
    config: PagingConfig,
    initialKey: K?,
    pagingSourceFactory: () -> PagingSource<K, V>
) {
    actual val pagingData: Flow<PagingData<V>> = AndroidPager(
        config = config,
        initialKey = initialKey,
        pagingSourceFactory = pagingSourceFactory
    ).flow
}