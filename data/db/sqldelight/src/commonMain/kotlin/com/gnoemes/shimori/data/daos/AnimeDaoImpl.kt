package com.gnoemes.shimori.data.daos

import app.cash.paging.PagingSource
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.paging3.QueryPagingSource
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.adapters.GenresAdapter
import com.gnoemes.shimori.data.adapters.LocalDateAdapter
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.db.api.daos.AnimeDao
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.util.SYNCER_RESULT_TAG
import com.gnoemes.shimori.data.util.anime
import com.gnoemes.shimori.data.util.animeListViewMapper
import com.gnoemes.shimori.data.util.animeWithTrack
import com.gnoemes.shimori.data.util.long
import com.gnoemes.shimori.data.util.syncRemoteIds
import com.gnoemes.shimori.data.util.syncerForEntity
import com.gnoemes.shimori.data.util.withTransaction
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class AnimeDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : AnimeDao() {

    private val syncer = syncerForEntity(
        this,
        { _, title -> title.name.takeIf { it.isNotEmpty() } },
        { _, remote, _ -> remote },
        logger
    )

    override suspend fun insert(sourceId: Long, remote: Anime) {
        db.withTransaction {
            remote.let {
                db.animeQueries.insert(
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
                )
                val localId = animeQueries.selectLastInsertedRowId().executeAsOne()
                syncRemoteIds(sourceId, localId, remote.id, syncDataType)
            }
        }
    }

    override suspend fun update(sourceId: Long, remote: Anime, local: Anime) {
        db.withTransaction {
            remote.let {
                animeQueries.update(
                    local.id,
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
                    it.status?.let { EnumColumnAdapter<TitleStatus>().encode(it) },
                    it.episodes,
                    it.episodesAired,
                    it.dateAired?.let(LocalDateAdapter::encode),
                    it.dateReleased?.let(LocalDateAdapter::encode),
                    it.ageRating.let { EnumColumnAdapter<AgeRating>().encode(it) },
                    it.description,
                    it.descriptionHtml,
                    it.franchise,
                    it.favorite,
                    it.topicId,
                    it.genres?.let(GenresAdapter::encode),
                    it.duration?.toLong(),
                    it.nextEpisode?.toLong(),
                )

                syncRemoteIds(sourceId, local.id, remote.id, syncDataType)
            }
        }
    }

    override suspend fun delete(sourceId: Long, local: Anime) {
        db.withTransaction {
            animeQueries.deleteById(local.id)
            sourceIdsSyncQueries.deleteByLocal(local.id, syncDataType.type)
        }
    }

    override suspend fun sync(sourceId: Long, remote: List<Anime>) {
        val result = syncer.sync(
            sourceId = sourceId,
            currentValues = db.animeQueries.queryAll(::anime).executeAsList(),
            networkValues = remote,
            removeNotMatched = false
        )

        logger.i(tag = SYNCER_RESULT_TAG) {
            "Anime sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }

    override suspend fun queryById(id: Long): Anime? {
        return db.animeQueries.queryById(id, ::anime)
            .executeAsOneOrNull()
    }

    override suspend fun queryAll(): List<Anime> {
        TODO("Not yet implemented")
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
    ): PagingSource<Int, PaginatedEntity> {

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

        return QueryPagingSource(
            countQuery = db.animeQueries.countWithStatus(status),
            transacter = db.animeQueries,
            context = dispatchers.io,
            queryProvider = ::query
        )
    }
}