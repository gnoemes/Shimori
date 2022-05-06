package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.base.database.daos.MangaDao
import com.gnoemes.shimori.data.base.entities.rate.RateSort
import com.gnoemes.shimori.data.base.entities.rate.RateSortOption
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.manga.Manga
import com.gnoemes.shimori.data.base.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.long
import com.gnoemes.shimori.data.shared.manga
import com.gnoemes.shimori.data.shared.mangaWithRate
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MangaDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
) : MangaDao() {

    override suspend fun insert(entity: Manga) {
        entity.let {
            db.mangaQueries.insert(
                it.shikimoriId,
                it.name,
                it.nameRu,
                it.nameEn,
                it.image?.original,
                it.image?.preview,
                it.image?.x96,
                it.image?.x48,
                it.url,
                it.mangaType?.type,
                it.rating,
                it.status,
                it.chapters,
                it.volumes,
                it.dateAired,
                it.dateReleased,
                it.ageRating,
                it.description,
                it.descriptionHtml,
                it.franchise,
                it.favorite,
                it.topicId,
                it.genres
            )
        }
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

    override fun observeById(id: Long): Flow<MangaWithRate?> {
        return db.mangaQueries.queryByIdWithRate(id, ::mangaWithRate)
            .asFlow()
            .map { it.executeAsOneOrNull() }
    }

    override suspend fun queryByStatus(status: RateStatus): List<MangaWithRate> {
        return db.mangaQueries.queryByStatus(status, ::mangaWithRate)
            .executeAsList()
    }

    override fun observeByStatus(status: RateStatus, sort: RateSort): Flow<List<MangaWithRate>> {
        return (when (sort.sortOption) {
            RateSortOption.NAME -> db.mangaQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                ::mangaWithRate
            )
            RateSortOption.PROGRESS -> db.mangaQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                ::mangaWithRate
            )
            RateSortOption.DATE_CREATED -> db.mangaQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                ::mangaWithRate
            )
            RateSortOption.DATE_UPDATED -> db.mangaQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                ::mangaWithRate
            )
            RateSortOption.DATE_AIRED -> db.mangaQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                ::mangaWithRate
            )
            RateSortOption.MY_SCORE -> db.mangaQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                ::mangaWithRate
            )
            RateSortOption.SIZE -> db.mangaQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                ::mangaWithRate
            )
            RateSortOption.RATING -> db.mangaQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                ::mangaWithRate
            )
        })
            .asFlow()
            .map { it.executeAsList() }
    }

    override fun paging(status: RateStatus, descending: Boolean, sortOption: RateSortOption) {
        TODO("Not yet implemented")
    }


}