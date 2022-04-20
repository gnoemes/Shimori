package com.gnoemes.shimori.data.base.database.daos

import com.gnoemes.shimori.data.base.entities.rate.RateSortOption
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.manga.Manga
import com.gnoemes.shimori.data.base.entities.titles.manga.MangaWithRate
import kotlinx.coroutines.flow.Flow

abstract class MangaDao : EntityDao<Manga>() {
    abstract suspend fun queryById(id: Long): Manga?
    abstract suspend fun queryAll(): List<Manga>
    abstract suspend fun queryAllWithStatus(): List<MangaWithRate>
    abstract suspend fun queryByStatus(status: RateStatus): List<MangaWithRate>

    abstract fun observeByStatus(status: RateStatus): Flow<List<MangaWithRate>>

    abstract fun paging(status: RateStatus, descending: Boolean, sortOption: RateSortOption)
}