package com.gnoemes.shimori.data.shared.paging

import com.gnoemes.shimori.data.paging.PagingSource
import com.gnoemes.shimori.data.paging.PagingState
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.properties.Delegates

internal abstract class QueryPagingSource<Key : Any, Value : Any> : PagingSource<Key, Value>(),
    Query.Listener {

    protected var currentQuery: Query<Value>? by Delegates.observable(null) { _, old, new ->
        old?.removeListener(this)
        new?.addListener(this)
    }

    init {
        registerInvalidatedCallback {
            currentQuery?.removeListener(this)
            currentQuery = null
        }
    }

    final override fun queryResultsChanged() = invalidate()
    final override fun getRefreshKey(state: PagingState<Key, Value>): Key? = null
}

internal fun <RowType : Any> QueryPaging(
    countQuery: Query<Long>,
    transacter: Transacter,
    dispatcher: CoroutineDispatcher,
    queryProvider: (limit: Long, offset: Long) -> Query<RowType>,
): PagingSource<Long, RowType> = OffsetQueryPagingSource(
    queryProvider, countQuery, transacter, dispatcher
)