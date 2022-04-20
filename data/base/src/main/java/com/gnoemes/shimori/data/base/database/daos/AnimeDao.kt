package com.gnoemes.shimori.data.base.database.daos

import com.gnoemes.shimori.data.base.entities.rate.RateSortOption
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.entities.titles.anime.AnimeWithRate
import kotlinx.coroutines.flow.Flow

abstract class AnimeDao : EntityDao<Anime>() {
    abstract suspend fun queryById(id: Long): Anime?
    abstract suspend fun queryAll(): List<Anime>
    abstract suspend fun queryAllWithStatus(): List<AnimeWithRate>
    abstract suspend fun queryByStatus(status: RateStatus): List<AnimeWithRate>

    abstract fun observeCalendar(): Flow<List<AnimeWithRate>>
    abstract fun observeByStatus(status: RateStatus): Flow<List<AnimeWithRate>>

    abstract fun paging(status: RateStatus, descending: Boolean, sortOption: RateSortOption)
}