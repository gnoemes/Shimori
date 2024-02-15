package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import kotlinx.coroutines.flow.Flow

abstract class MangaDao : EntityDao<Manga> {
    abstract fun queryById(id: Long): Manga?
    abstract fun queryAll(): List<Manga>
    abstract fun queryByStatus(status: TrackStatus): List<MangaWithTrack>

    abstract fun observeById(id: Long): Flow<MangaWithTrack?>

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Int, PaginatedEntity>
}