package com.gnoemes.shimori.data.shared.paging

import androidx.paging.PagingSource
import com.gnoemes.shimori.data.paging.LoadParams
import com.gnoemes.shimori.data.paging.LoadResult
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal actual class OffsetQueryPagingSource<RowType : Any> actual constructor(
    private val queryProvider: (limit: Long, offset: Long) -> Query<RowType>,
    private val countQuery: Query<Long>,
    private val transacter: Transacter,
    private val dispatcher: CoroutineDispatcher,
) : QueryPagingSource<Long, RowType>() {
    actual override val jumpingSupported get() = true

    actual override suspend fun load(
        params: LoadParams<Long>
    ): LoadResult<Long, RowType> = withContext(dispatcher) {
        try {
            val key = params.key ?: 0L
            transacter.transactionWithResult {
                val count = countQuery.executeAsOne()
                if (count != 0L && key >= count) throw IndexOutOfBoundsException()

                val loadSize = if (key < 0) params.loadSize + key else params.loadSize

                val data = queryProvider(loadSize.toLong(), maxOf(0, key))
                    .also { currentQuery = it }
                    .executeAsList()

                PagingSource.LoadResult.Page(
                    data = data,
                    // allow one, and only one negative prevKey in a paging set. This is done for
                    // misaligned prepend queries to avoid duplicates.
                    prevKey = if (key <= 0L) null else key - params.loadSize,
                    nextKey = if (key + params.loadSize >= count) null else key + params.loadSize,
                    itemsBefore = maxOf(0L, key).toInt(),
                    itemsAfter = maxOf(0, (count - (key + params.loadSize))).toInt()
                )
            }
        } catch (e: Exception) {
            if (e is IndexOutOfBoundsException) throw e
            PagingSource.LoadResult.Error(e)
        }
    }
}