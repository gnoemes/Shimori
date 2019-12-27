package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.data.daos.AnimeDao
import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.sync.syncerForEntity
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.AnimeWithRate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeStore @Inject constructor(
    private val inserter: EntityInserter,
    private val runner: DatabaseTransactionRunner,
    private val animeDao: AnimeDao
) {

    private val syncer = syncerForEntity(
            animeDao,
            { it.shikimoriId },
            { entity, id -> entity.copy(id = id ?: 0) }
    )

    fun observeCalendar(filter: String?): Flow<List<AnimeWithRate>> = when {
        filter.isNullOrBlank() -> animeDao.observeCalendar()
        else -> animeDao.observeCalendarFilter("*$filter*")
    }

    suspend fun updateAnimes(animes: List<Anime>) = runner {
        syncer.sync(animeDao.queryAll(), animes, removeNotMatched = false)
    }
}