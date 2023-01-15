package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.SourceDataType
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.paging.PagingSource
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
    ): PagingSource<Long, PaginatedEntity>
}