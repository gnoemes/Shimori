package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.base.database.daos.MangaDao
import com.gnoemes.shimori.data.base.entities.rate.RateSortOption
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.manga.Manga
import com.gnoemes.shimori.data.base.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.manga
import com.gnoemes.shimori.data.shared.mangaDao
import com.gnoemes.shimori.data.shared.mangaWithRate
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MangaDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
) : MangaDao() {

    override suspend fun insert(entity: Manga) {
        db.mangaQueries.insert(mangaDao(entity))
    }

    override suspend fun deleteEntity(entity: Manga) {
        db.mangaQueries.deleteById(entity.id)
    }

    override suspend fun queryById(id: Long): Manga? {
        return db.mangaQueries.queryById(id, ::manga)
            .executeAsOneOrNull()
    }

    override suspend fun queryAll(): List<Manga> {
        TODO("Not yet implemented")
    }

    override suspend fun queryAllWithStatus(): List<MangaWithRate> {
        return db.mangaQueries.queryAllWithStatus(::mangaWithRate)
            .executeAsList()
    }

    override suspend fun queryByStatus(status: RateStatus): List<MangaWithRate> {
        return db.mangaQueries.queryByStatus(status, ::mangaWithRate)
            .executeAsList()
    }

    override fun observeByStatus(status: RateStatus): Flow<List<MangaWithRate>> {
        return db.mangaQueries.queryByStatus(status, ::mangaWithRate)
            .asFlow()
            .map { it.executeAsList() }
    }

    override fun paging(status: RateStatus, descending: Boolean, sortOption: RateSortOption) {
        TODO("Not yet implemented")
    }


}