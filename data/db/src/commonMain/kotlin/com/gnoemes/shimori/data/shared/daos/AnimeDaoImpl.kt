package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.AnimeDao
import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.core.entities.app.SyncTarget
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
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

    override suspend fun queryIdsBySyncTargets(
        targets: List<SyncTarget>
    ): List<Pair<SyncTarget, Long>> {
        val ids = targets.map { it.id }
        //TODO switch between apis
        return when (targets.first().api) {
            //TODO refactor sync apis interaction
            SyncApi.Shikimori -> db.animeQueries.queryLocalAndShikimoriIdsByShikimoriIds(
                ids,
                mapper = { shikimori_id: Long, id: Long ->
                    SyncTarget(SyncApi.Shikimori, shikimori_id) to id
                }
            )
                .executeAsList()
        }
    }

    override suspend fun queryByStatus(status: TrackStatus): List<AnimeWithTrack> {
        return db.animeQueries.queryByStatus(status, ::animeWithTrack)
            .executeAsList()
    }

    override fun observeById(id: Long): Flow<AnimeWithTrack?> {
        return db.animeQueries.queryByIdWithTrack(id, ::animeWithTrack)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeCalendar(): Flow<List<AnimeWithTrack>> {
        TODO("Not yet implemented")
    }

    override fun paging(
        status: TrackStatus,
        sort: ListSort,
    ): PagingSource<Long, PaginatedEntity> {

        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            ListSortOption.NAME -> db.animeListViewQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                animeListViewMapper
            )
            ListSortOption.PROGRESS -> db.animeListViewQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                animeListViewMapper
            )
            ListSortOption.DATE_CREATED -> db.animeListViewQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                animeListViewMapper
            )
            ListSortOption.DATE_UPDATED -> db.animeListViewQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                animeListViewMapper
            )
            ListSortOption.DATE_AIRED -> db.animeListViewQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                animeListViewMapper
            )
            ListSortOption.MY_SCORE -> db.animeListViewQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                animeListViewMapper
            )
            ListSortOption.SIZE -> db.animeListViewQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                animeListViewMapper
            )
            ListSortOption.RATING -> db.animeListViewQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                animeListViewMapper
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