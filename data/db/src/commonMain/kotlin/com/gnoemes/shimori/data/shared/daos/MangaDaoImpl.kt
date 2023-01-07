package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.MangaDao
import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.core.entities.app.SyncTarget
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateSortOption
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.paging.PagingSource
import com.gnoemes.shimori.data.shared.*
import com.gnoemes.shimori.data.shared.paging.QueryPaging
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.system.measureTimeMillis

internal class MangaDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : MangaDao() {

    private val syncer = syncerForEntity(
        this,
        { it.shikimoriId },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

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

    override suspend fun update(entity: Manga) {
        entity.let {
            db.mangaQueries.update(
                it.id,
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
                it.status?.let(TitleStatusAdapter::encode),
                it.chapters,
                it.volumes,
                it.dateAired?.let(LocalDateAdapter::encode),
                it.dateReleased?.let(LocalDateAdapter::encode),
                it.ageRating.let(AgeRatingAdapter::encode),
                it.description,
                it.descriptionHtml,
                it.franchise,
                it.favorite,
                it.topicId,
                it.genres?.let(GenresAdapter::encode),
            )
        }
    }

    override suspend fun delete(entity: Manga) {
        db.mangaQueries.deleteById(entity.id)
    }

    override suspend fun queryById(id: Long): Manga? {
        return db.mangaQueries.queryById(id, ::manga)
            .executeAsOneOrNull()
    }

    override suspend fun queryAll(): List<Manga> {
        TODO("Not yet implemented")
    }

    override suspend fun queryIdsBySyncTargets(
        targets: List<SyncTarget>
    ): List<Pair<SyncTarget, Long>> {
        val ids = targets.map { it.id }
        //TODO switch between apis
        return when (targets.first().api) {
            //TODO refactor sync apis interaction
            SyncApi.Shikimori -> db.mangaQueries.queryLocalAndShikimoriIdsByShikimoriIds(
                ids,
                mapper = { shikimori_id: Long, id: Long ->
                    SyncTarget(SyncApi.Shikimori, shikimori_id) to id
                }
            )
                .executeAsList()
        }
    }

    override suspend fun insertOrUpdate(entities: List<Manga>) {
        val result: ItemSyncerResult<Manga>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.mangaQueries.queryAll(::manga).executeAsList(),
                networkValues = entities,
                removeNotMatched = false
            )
        }

        logger.i(
            "Manga sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override fun observeById(id: Long): Flow<MangaWithRate?> {
        return db.mangaQueries.queryByIdWithRate(id, ::mangaWithRate)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override suspend fun queryByStatus(status: RateStatus): List<MangaWithRate> {
        return db.mangaQueries.queryByStatus(status, ::mangaWithRate)
            .executeAsList()
    }


    override fun paging(status: RateStatus, sort: RateSort): PagingSource<Long, PaginatedEntity> {
        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            RateSortOption.NAME -> db.mangaQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::mangaWithRate
            )
            RateSortOption.PROGRESS -> db.mangaQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::mangaWithRate
            )
            RateSortOption.DATE_CREATED -> db.mangaQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::mangaWithRate
            )
            RateSortOption.DATE_UPDATED -> db.mangaQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::mangaWithRate
            )
            RateSortOption.DATE_AIRED -> db.mangaQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::mangaWithRate
            )
            RateSortOption.MY_SCORE -> db.mangaQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::mangaWithRate
            )
            RateSortOption.SIZE -> db.mangaQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::mangaWithRate
            )
            RateSortOption.RATING -> db.mangaQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::mangaWithRate
            )
        }

        return QueryPaging(
            countQuery = db.mangaQueries.countWithStatus(status),
            transacter = db.mangaQueries,
            dispatcher = dispatchers.io,
            queryProvider = ::query
        )
    }
}