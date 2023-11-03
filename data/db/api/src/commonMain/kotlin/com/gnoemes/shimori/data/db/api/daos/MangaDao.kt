package com.gnoemes.shimori.data.db.api.daos

import app.cash.paging.PagingSource
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.TrackStatus
import kotlinx.coroutines.flow.Flow

abstract class MangaDao : SourceSyncEntityDao<Manga>(SourceDataType.Manga) {
    abstract suspend fun sync(sourceId: Long, remote: List<Manga>)
    abstract suspend fun queryById(id: Long): Manga?
    abstract suspend fun queryAll(): List<Manga>
    abstract suspend fun queryByStatus(status: TrackStatus): List<MangaWithTrack>

    abstract fun observeById(id: Long): Flow<MangaWithTrack?>

    abstract fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Int, PaginatedEntity>
}