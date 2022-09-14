package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.AnimeDao
import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateSortOption
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithRate
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.paging.PagingSource
import com.gnoemes.shimori.data.shared.*
import com.gnoemes.shimori.data.shared.paging.QueryPaging
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.system.measureTimeMillis

internal class AnimeDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : AnimeDao() {

    private val syncer = syncerForEntity(
        this,
        { it.shikimoriId },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    override suspend fun insert(entity: Anime) {
        entity.let {
            db.animeQueries.insert(
                it.shikimoriId,
                it.name,
                it.nameRu,
                it.nameEn,
                it.image?.original,
                it.image?.preview,
                it.image?.x96,
                it.image?.x48,
                it.url,
                it.animeType?.type,
                it.rating,
                it.status,
                it.episodes,
                it.episodesAired,
                it.dateAired,
                it.dateReleased,
                it.ageRating,
                it.description,
                it.descriptionHtml,
                it.franchise,
                it.favorite,
                it.topicId,
                it.genres,
                it.duration,
                it.nextEpisode,
                it.nextEpisodeDate,
                it.nextEpisodeEndDate,
            )
        }
    }

    override suspend fun update(entity: Anime) {
        entity.let {
            db.animeQueries.update(
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
                it.animeType?.type,
                it.rating,
                it.status?.let(TitleStatusAdapter::encode),
                it.episodes,
                it.episodesAired,
                it.dateAired?.let(LocalDateAdapter::encode),
                it.dateReleased?.let(LocalDateAdapter::encode),
                it.ageRating.let(AgeRatingAdapter::encode),
                it.description,
                it.descriptionHtml,
                it.franchise,
                it.favorite,
                it.topicId,
                it.genres?.let(GenresAdapter::encode),
                it.duration?.toLong(),
                it.nextEpisode?.toLong(),
                it.nextEpisodeDate?.let(InstantAdapter::encode),
                it.nextEpisodeEndDate?.let(InstantAdapter::encode),
            )
        }
    }

    override suspend fun delete(entity: Anime) {
        db.animeQueries.deleteById(entity.id)
    }

    override suspend fun insertOrUpdate(entities: List<Anime>) {
        val result: ItemSyncerResult<Anime>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.animeQueries.queryAll(::anime).executeAsList(),
                networkValues = entities,
                removeNotMatched = false
            )
        }

        logger.i(
            "Anime sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override suspend fun queryById(id: Long): Anime? {
        return db.animeQueries.queryById(id, ::anime)
            .executeAsOneOrNull()
    }

    override suspend fun queryAll(): List<Anime> {
        TODO("Not yet implemented")
    }

    override suspend fun queryByStatus(status: RateStatus): List<AnimeWithRate> {
        return db.animeQueries.queryByStatus(status, ::animeWithRate)
            .executeAsList()
    }

    override fun observeById(id: Long): Flow<AnimeWithRate?> {
        return db.animeQueries.queryByIdWithRate(id, ::animeWithRate)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeCalendar(): Flow<List<AnimeWithRate>> {
        TODO("Not yet implemented")
    }

    override fun paging(
        status: RateStatus,
        sort: RateSort,
    ): PagingSource<Long, PaginatedEntity> {

        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            RateSortOption.NAME -> db.animeQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::animeWithRate
            )
            RateSortOption.PROGRESS -> db.animeQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::animeWithRate
            )
            RateSortOption.DATE_CREATED -> db.animeQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::animeWithRate
            )
            RateSortOption.DATE_UPDATED -> db.animeQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::animeWithRate
            )
            RateSortOption.DATE_AIRED -> db.animeQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::animeWithRate
            )
            RateSortOption.MY_SCORE -> db.animeQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::animeWithRate
            )
            RateSortOption.SIZE -> db.animeQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::animeWithRate
            )
            RateSortOption.RATING -> db.animeQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ::animeWithRate
            )
        }

        return QueryPaging(
            countQuery = db.animeQueries.countWithStatus(status),
            transacter = db.animeQueries,
            dispatcher = dispatchers.io,
            queryProvider = ::query
        )
    }
}