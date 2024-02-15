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
import com.gnoemes.shimori.data.db.api.daos.MangaDao
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.util.long
import com.gnoemes.shimori.data.util.manga
import com.gnoemes.shimori.data.util.mangaListViewMapper
import com.gnoemes.shimori.data.util.mangaWithTrack
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class MangaDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : MangaDao(), SqlDelightEntityDao<Manga> {

    override fun insert(entity: Manga): Long {
        entity.let {
            db.mangaQueries.insert(
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

        return db.mangaQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: Manga) {
        entity.let {
            db.mangaQueries.update(
                it.id,
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
                it.status?.let { EnumColumnAdapter<TitleStatus>().encode(it) },
                it.chapters,
                it.volumes,
                it.dateAired?.let(LocalDateAdapter::encode),
                it.dateReleased?.let(LocalDateAdapter::encode),
                it.ageRating.let { EnumColumnAdapter<AgeRating>().encode(it) },
                it.description,
                it.descriptionHtml,
                it.franchise,
                it.favorite,
                it.topicId,
                it.genres?.let(GenresAdapter::encode),
            )
        }
    }

    override fun delete(entity: Manga) {
        return db.mangaQueries.deleteById(entity.id)
    }

    override fun queryById(id: Long): Manga? {
        return db.mangaQueries.queryById(id, ::manga)
            .executeAsOneOrNull()
    }

    override fun queryAll(): List<Manga> {
        return db.mangaQueries.queryAll(::manga).executeAsList()
    }

    override fun observeById(id: Long): Flow<MangaWithTrack?> {
        return db.mangaQueries.queryByIdWithTrack(id, ::mangaWithTrack)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun queryByStatus(status: TrackStatus): List<MangaWithTrack> {
        return db.mangaQueries.queryByStatus(status, ::mangaWithTrack)
            .executeAsList()
    }


    override fun paging(status: TrackStatus, sort: ListSort): PagingSource<Int, PaginatedEntity> {
        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            ListSortOption.NAME -> db.mangaListViewQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.PROGRESS -> db.mangaListViewQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.DATE_CREATED -> db.mangaListViewQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.DATE_UPDATED -> db.mangaListViewQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.DATE_AIRED -> db.mangaListViewQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.MY_SCORE -> db.mangaListViewQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.SIZE -> db.mangaListViewQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.RATING -> db.mangaListViewQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
        }

        return QueryPagingSource(
            countQuery = db.mangaQueries.countWithStatus(status),
            transacter = db.mangaQueries,
            context = dispatchers.io,
            queryProvider = ::query
        )
    }
}