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
import com.gnoemes.shimori.data.db.api.daos.RanobeDao
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.util.long
import com.gnoemes.shimori.data.util.ranobe
import com.gnoemes.shimori.data.util.ranobeListViewMapper
import com.gnoemes.shimori.data.util.ranobeWithTrack
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class RanobeDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : RanobeDao(), SqlDelightEntityDao<Ranobe> {

    override fun insert(entity: Ranobe): Long {
        entity.let {
            db.ranobeQueries.insert(
                it.name,
                it.nameRu,
                it.nameEn,
                it.image?.original,
                it.image?.preview,
                it.image?.x96,
                it.image?.x48,
                it.url,
                it.ranobeType?.type,
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

        return db.ranobeQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: Ranobe) {
        entity.let {
            db.ranobeQueries.update(
                it.id,
                it.name,
                it.nameRu,
                it.nameEn,
                it.image?.original,
                it.image?.preview,
                it.image?.x96,
                it.image?.x48,
                it.url,
                it.ranobeType?.type,
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

    override fun delete(entity: Ranobe) {
        return db.ranobeQueries.deleteById(entity.id)
    }

    override fun queryById(id: Long): Ranobe? {
        return db.ranobeQueries.queryById(id, ::ranobe)
            .executeAsOneOrNull()
    }

    override fun queryAll(): List<Ranobe> {
        return db.ranobeQueries.queryAll(::ranobe).executeAsList()
    }

    override fun queryByStatus(status: TrackStatus): List<RanobeWithTrack> {
        return db.ranobeQueries.queryByStatus(status, ::ranobeWithTrack)
            .executeAsList()
    }

    override fun observeById(id: Long): Flow<RanobeWithTrack?> {
        return db.ranobeQueries.queryByIdWithTrack(id, ::ranobeWithTrack)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun paging(status: TrackStatus, sort: ListSort): PagingSource<Int, PaginatedEntity> {

        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            ListSortOption.NAME -> db.ranobeListViewQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )

            ListSortOption.PROGRESS -> db.ranobeListViewQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )

            ListSortOption.DATE_CREATED -> db.ranobeListViewQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )

            ListSortOption.DATE_UPDATED -> db.ranobeListViewQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )

            ListSortOption.DATE_AIRED -> db.ranobeListViewQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )

            ListSortOption.MY_SCORE -> db.ranobeListViewQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )

            ListSortOption.SIZE -> db.ranobeListViewQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )

            ListSortOption.RATING -> db.ranobeListViewQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                ranobeListViewMapper
            )
        }

        return QueryPagingSource(
            countQuery = db.ranobeQueries.countWithStatus(status),
            transacter = db.ranobeQueries,
            context = dispatchers.io,
            queryProvider = ::query
        )
    }
}