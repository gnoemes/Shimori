package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.paging.PagingSource
import kotlinx.coroutines.flow.Flow

abstract class MangaDao : EntityDao<Manga>() {
    abstract suspend fun queryById(id: Long): Manga?
    abstract suspend fun queryAll(): List<Manga>
    abstract suspend fun queryAllWithStatus(): List<MangaWithRate>
    abstract suspend fun queryByStatus(status: RateStatus): List<MangaWithRate>

    abstract fun observeById(id: Long): Flow<MangaWithRate?>

    abstract fun paging(
        status: RateStatus,
        sort: RateSort,
    ): PagingSource<Long, PaginatedEntity>
}