package com.gnoemes.shimori.data.daos

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.paging3.QueryPagingSource
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.db.api.daos.MangaAndRanobeQueryableDao
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.util.long
import com.gnoemes.shimori.data.util.mangaListViewMapper
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
@ContributesBinding(AppScope::class, boundType = MangaAndRanobeQueryableDao::class)
class MangaAndRanobeQueryableDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : MangaAndRanobeQueryableDao(), SqlDelightQueryableDao<ShimoriTitleEntity> {

    override fun observeStatusExists(status: TrackStatus): Flow<Boolean> {
        return db.mangaAndRanobeUnionQueries.countWithStatus(status)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .map { it != null && it > 0 }
            .flowOn(dispatchers.io)
    }

    override fun paging(status: TrackStatus, sort: ListSort): PagingSource<Int, PaginatedEntity> {
        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            ListSortOption.NAME -> db.mangaAndRanobeListViewQueries.queryByStatusSortName(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.PROGRESS -> db.mangaAndRanobeListViewQueries.queryByStatusSortProgress(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.DATE_CREATED -> db.mangaAndRanobeListViewQueries.queryByStatusSortDateCreated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.DATE_UPDATED -> db.mangaAndRanobeListViewQueries.queryByStatusSortDateUpdated(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.DATE_AIRED -> db.mangaAndRanobeListViewQueries.queryByStatusSortDateAired(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.MY_SCORE -> db.mangaAndRanobeListViewQueries.queryByStatusSortScore(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.SIZE -> db.mangaAndRanobeListViewQueries.queryByStatusSortSize(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )

            ListSortOption.RATING -> db.mangaAndRanobeListViewQueries.queryByStatusSortRating(
                status,
                sort.isDescending.long,
                limit,
                offset,
                mangaListViewMapper
            )
        }

        return QueryPagingSource(
            countQuery = db.mangaAndRanobeUnionQueries.countWithStatus(status),
            transacter = db.mangaAndRanobeUnionQueries,
            context = dispatchers.io,
            queryProvider = ::query
        )
    }

}