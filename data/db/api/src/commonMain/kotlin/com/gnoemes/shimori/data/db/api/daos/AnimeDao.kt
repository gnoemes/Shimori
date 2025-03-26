package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import kotlinx.coroutines.flow.Flow

abstract class AnimeDao : EntityDao<Anime> {
    abstract fun queryById(id: Long): Anime?
    abstract fun queryAll(): List<Anime>
    abstract fun queryByStatus(status: TrackStatus): List<AnimeWithTrack>

    abstract fun observeById(id: Long): Flow<AnimeWithTrack?>
    abstract fun observeCalendar(): Flow<List<AnimeWithTrack>>

    abstract fun observeStatusExists(status: TrackStatus): Flow<Boolean>

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Int, PaginatedEntity>
}