package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus

abstract class MangaAndRanobeQueryableDao : QueryableDao<ShimoriTitleEntity> {

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Int, PaginatedEntity>
}