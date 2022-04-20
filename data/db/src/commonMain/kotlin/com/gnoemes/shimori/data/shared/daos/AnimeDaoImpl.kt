package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.base.database.daos.AnimeDao
import com.gnoemes.shimori.data.base.entities.rate.RateSortOption
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.entities.titles.anime.AnimeWithRate
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.anime
import com.gnoemes.shimori.data.shared.animeDao
import com.gnoemes.shimori.data.shared.animeWithRate
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AnimeDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
) : AnimeDao() {

    override suspend fun insert(entity: Anime) {
        db.animeQueries.insert(animeDao(entity))
    }

    override suspend fun deleteEntity(entity: Anime) {
        db.animeQueries.deleteById(entity.id)
    }

    override suspend fun queryById(id: Long): Anime? {
        return db.animeQueries.queryById(id, ::anime)
            .executeAsOneOrNull()
    }

    override suspend fun queryAll(): List<Anime> {
        TODO("Not yet implemented")
    }

    override suspend fun queryAllWithStatus(): List<AnimeWithRate> {
        return db.animeQueries.queryAllWithStatus(::animeWithRate)
            .executeAsList()
    }

    override suspend fun queryByStatus(status: RateStatus): List<AnimeWithRate> {
        return db.animeQueries.queryByStatus(status, ::animeWithRate)
            .executeAsList()
    }

    override fun observeCalendar(): Flow<List<AnimeWithRate>> {
        TODO("Not yet implemented")
    }

    override fun observeByStatus(status: RateStatus): Flow<List<AnimeWithRate>> {
        return db.animeQueries.queryByStatus(status, ::animeWithRate)
            .asFlow()
            .map { it.executeAsList() }
    }

    override fun paging(status: RateStatus, descending: Boolean, sortOption: RateSortOption) {
        TODO("Not yet implemented")
    }


}