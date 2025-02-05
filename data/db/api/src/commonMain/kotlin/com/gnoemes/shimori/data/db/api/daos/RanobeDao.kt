package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import kotlinx.coroutines.flow.Flow

abstract class RanobeDao : EntityDao<Ranobe> {
    abstract fun queryById(id: Long): Ranobe?
    abstract fun queryAll(): List<Ranobe>
    abstract fun queryByStatus(status: TrackStatus): List<RanobeWithTrack>

    abstract fun observeById(id: Long): Flow<RanobeWithTrack?>

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Int, PaginatedEntity>
}