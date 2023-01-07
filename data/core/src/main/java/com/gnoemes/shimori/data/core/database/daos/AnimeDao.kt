package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.SyncTarget
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithRate
import com.gnoemes.shimori.data.paging.PagingSource
import kotlinx.coroutines.flow.Flow

abstract class AnimeDao : EntityDao<Anime>() {
    abstract suspend fun queryById(id: Long): Anime?
    abstract suspend fun queryIdsBySyncTargets(targets: List<SyncTarget>): List<Pair<SyncTarget, Long>>
    abstract suspend fun queryAll(): List<Anime>
    abstract suspend fun queryByStatus(status: RateStatus): List<AnimeWithRate>

    abstract fun observeById(id: Long): Flow<AnimeWithRate?>
    abstract fun observeCalendar(): Flow<List<AnimeWithRate>>

    abstract fun paging(
        status: RateStatus,
        sort: RateSort,
    ): PagingSource<Long, PaginatedEntity>
}