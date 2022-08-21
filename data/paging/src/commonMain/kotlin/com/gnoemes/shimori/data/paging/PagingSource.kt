package com.gnoemes.shimori.data.paging


expect abstract class PagingSource<Key : Any, Value : Any>() {
    fun registerInvalidatedCallback(onInvalidatedCallback: () -> Unit)
    fun invalidate()
    abstract suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value>
    abstract fun getRefreshKey(state: PagingState<Key, Value>): Key?
    open val jumpingSupported: Boolean
}

expect sealed class LoadParams<Key : Any> {
    val loadSize: Int
    val placeholdersEnabled: Boolean

    abstract val key: Key?
}

expect sealed class LoadResult<Key : Any, Value : Any>
expect class PagingState<Key : Any, Value : Any>