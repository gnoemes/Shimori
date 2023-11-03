package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import kotlinx.coroutines.flow.Flow

abstract class AnimeDao : SourceSyncEntityDao<Anime>(SourceDataType.Anime) {
    abstract suspend fun sync(sourceId: Long, remote: List<Anime>)
    abstract suspend fun queryById(id: Long): Anime?
    abstract suspend fun queryAll(): List<Anime>
    abstract suspend fun queryByStatus(status: TrackStatus): List<AnimeWithTrack>

    abstract fun observeById(id: Long): Flow<AnimeWithTrack?>
    abstract fun observeCalendar(): Flow<List<AnimeWithTrack>>

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Int, PaginatedEntity>
}