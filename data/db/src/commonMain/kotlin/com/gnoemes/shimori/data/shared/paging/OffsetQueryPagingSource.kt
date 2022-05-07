package com.gnoemes.shimori.data.shared.paging

import com.gnoemes.shimori.data.paging.LoadParams
import com.gnoemes.shimori.data.paging.LoadResult
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.CoroutineDispatcher

internal expect class OffsetQueryPagingSource<RowType : Any>(
    queryProvider: (limit: Long, offset: Long) -> Query<RowType>,
    countQuery: Query<Long>,
    transacter: Transacter,
    dispatcher: CoroutineDispatcher
) : QueryPagingSource<Long, RowType> {
    override val jumpingSupported: Boolean
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, RowType>
}