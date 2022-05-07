package com.gnoemes.shimori.data.paging

import kotlinx.coroutines.flow.Flow

expect class Pager<K : Any, V : Any>(
    config: PagingConfig,
    initialKey: K? = null,
    pagingSourceFactory: () -> PagingSource<K, V>
) {
    val pagingData: Flow<PagingData<V>>
}