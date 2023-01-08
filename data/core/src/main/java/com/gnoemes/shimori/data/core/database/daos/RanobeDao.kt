package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.paging.PagingSource
import kotlinx.coroutines.flow.Flow

abstract class RanobeDao : EntityDao<Ranobe>() {
    abstract suspend fun queryById(id: Long): Ranobe?
    abstract suspend fun queryAll(): List<Ranobe>
    abstract suspend fun queryByStatus(status: TrackStatus): List<RanobeWithTrack>

    abstract fun observeById(id: Long): Flow<RanobeWithTrack?>

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Long, PaginatedEntity>
}