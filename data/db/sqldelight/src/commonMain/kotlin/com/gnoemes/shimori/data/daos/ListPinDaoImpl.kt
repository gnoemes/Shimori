package com.gnoemes.shimori.data.daos

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.paging3.QueryPagingSource
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.app.ListPin
import com.gnoemes.shimori.data.db.api.daos.ListPinDao
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.long
import com.gnoemes.shimori.data.util.pinPaginated
import com.gnoemes.shimori.logging.api.Logger
import comgnoemesshimoridatadb.data.Pinned
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ListPinDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : ListPinDao, SqlDelightEntityDao<ListPin> {

    override fun insert(entity: ListPin): Long {
        entity.let {
            db.listPinQueries.insert(
                it.targetId,
                it.targetType
            )
        }
        return db.listPinQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: ListPin) {
        entity.let {
            db.listPinQueries.update(
                Pinned(
                    it.id,
                    it.targetId,
                    it.targetType
                )
            )
        }
    }

    override fun delete(entity: ListPin) {
        db.listPinQueries.deleteById(entity.id)
    }

    override fun pin(targetId: Long, targetType: TrackTargetType, pin: Boolean): Boolean {
        val local = db.listPinQueries.queryByTarget(targetId, targetType).executeAsOneOrNull()
        if (pin) {
            if (local != null) return true
            db.listPinQueries.insert(targetId, targetType)
        } else {
            if (local == null) return false
            db.listPinQueries.deleteById(local.id)
        }

        return pin
    }

    override fun togglePin(targetId: Long, targetType: TrackTargetType): Boolean {
        val local = db.listPinQueries.queryByTarget(targetId, targetType).executeAsOneOrNull()
        return if (local != null) {
            db.listPinQueries.deleteById(local.id)
            false
        } else {
            db.listPinQueries.insert(targetId, targetType)
            true
        }
    }

    override fun observePinExist(targetId: Long, targetType: TrackTargetType): Flow<Boolean> {
        return db.listPinQueries.queryByTarget(targetId, targetType)
            .asFlow()
            .map { it.executeAsOneOrNull() != null }
            .flowOn(dispatchers.io)
    }

    override fun observePinsExist(): Flow<Boolean> {
        return db.listPinQueries.queryCount()
            .asFlow()
            .map { it.executeAsOne() > 0 }
            .flowOn(dispatchers.io)
    }

    override fun paging(sort: ListSort): PagingSource<Int, PaginatedEntity> {
        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            ListSortOption.NAME -> db.pinnedListViewQueries.querySortName(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )

            ListSortOption.PROGRESS -> db.pinnedListViewQueries.querySortProgress(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )

            ListSortOption.DATE_CREATED -> db.pinnedListViewQueries.querySortDateCreated(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )

            ListSortOption.DATE_UPDATED -> db.pinnedListViewQueries.querySortDateUpdated(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )

            ListSortOption.DATE_AIRED -> db.pinnedListViewQueries.querySortDateAired(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )

            ListSortOption.MY_SCORE -> db.pinnedListViewQueries.querySortScore(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )

            ListSortOption.RATING -> db.pinnedListViewQueries.querySortRating(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )

            else -> throw IllegalArgumentException("$sort is not supported yet")
        }


        return QueryPagingSource(
            countQuery = db.listPinQueries.queryCount(),
            transacter = db.listPinQueries,
            context = dispatchers.io,
            queryProvider = ::query
        )
    }
}