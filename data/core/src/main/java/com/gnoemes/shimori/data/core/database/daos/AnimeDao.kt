package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.SyncTarget
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.paging.PagingSource
import kotlinx.coroutines.flow.Flow

abstract class AnimeDao : EntityDao<Anime>() {
    abstract suspend fun queryById(id: Long): Anime?
    abstract suspend fun queryIdsBySyncTargets(targets: List<SyncTarget>): List<Pair<SyncTarget, Long>>
    abstract suspend fun queryAll(): List<Anime>
    abstract suspend fun queryByStatus(status: TrackStatus): List<AnimeWithTrack>

    abstract fun observeById(id: Long): Flow<AnimeWithTrack?>
    abstract fun observeCalendar(): Flow<List<AnimeWithTrack>>

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Long, PaginatedEntity>
}