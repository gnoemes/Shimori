package com.gnoemes.shimori.data.daos

import app.cash.paging.PagingSource
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.paging3.QueryPagingSource
import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.adapters.LocalDateAdapter
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.db.api.daos.AnimeDao
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.util.anime
import com.gnoemes.shimori.data.util.animeListViewMapper
import com.gnoemes.shimori.data.util.animeWithTrack
import com.gnoemes.shimori.data.util.long
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = AnimeDao::class)
class AnimeDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : AnimeDao(), SqlDelightEntityDao<Anime> {

    override fun insert(entity: Anime): Long {
        entity.let {
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
                it.duration,
                it.nextEpisode,
                it.nextEpisodeDate,
            )
        }

        return db.animeQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: Anime) {
        entity.let {
            db.animeQueries.update(
                entity.id,
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
                it.duration?.toLong(),
                it.nextEpisode?.toLong(),
            )
        }
    }

    override fun delete(entity: Anime) {
        return db.animeQueries.deleteById(entity.id)
    }

    override fun queryById(id: Long): Anime? {
        return db.animeQueries.queryById(id, ::anime)
            .executeAsOneOrNull()
    }

    override fun queryAll(): List<Anime> {
        return db.animeQueries.queryAll(::anime).executeAsList()
    }

    override fun queryByStatus(status: TrackStatus): List<AnimeWithTrack> {
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

    override fun observeStatusExists(status: TrackStatus): Flow<Boolean> {
        return db.animeQueries.countWithStatus(status)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .map { it != null && it > 0 }
            .flowOn(dispatchers.io)
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