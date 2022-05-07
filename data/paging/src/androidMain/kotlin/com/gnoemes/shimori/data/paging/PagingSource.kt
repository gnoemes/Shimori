package com.gnoemes.shimori.data.paging

actual typealias PagingSource<Key, Value> = androidx.paging.PagingSource<Key, Value>

actual typealias LoadParams<Key> = androidx.paging.PagingSource.LoadParams<Key>
actual typealias LoadResult<Key, Value> = androidx.paging.PagingSource.LoadResult<Key, Value>
actual typealias PagingState<Key, Value> = androidx.paging.PagingState<Key, Value>