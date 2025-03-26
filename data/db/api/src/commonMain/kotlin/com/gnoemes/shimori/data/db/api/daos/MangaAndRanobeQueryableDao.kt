package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import kotlinx.coroutines.flow.Flow

abstract class MangaAndRanobeQueryableDao : QueryableDao<ShimoriTitleEntity> {

    abstract fun observeStatusExists(status: TrackStatus): Flow<Boolean>

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Int, PaginatedEntity>
}