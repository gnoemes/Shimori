package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.ListPinDao
import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.ListPin
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateSortOption
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.paging.PagingSource
import com.gnoemes.shimori.data.shared.long
import com.gnoemes.shimori.data.shared.paging.QueryPaging
import com.gnoemes.shimori.data.shared.pinPaginated
import com.squareup.sqldelight.runtime.coroutines.asFlow
import comgnoemesshimoridatadb.data.Pinned
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class ListPinDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : ListPinDao() {

    override suspend fun insert(entity: ListPin) {
        entity.let {
            db.listPinQueries.insert(
                it.targetId,
                it.targetType
            )
        }
    }

    override suspend fun update(entity: ListPin) {
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

    override suspend fun delete(entity: ListPin) {
        db.listPinQueries.deleteById(entity.id)
    }

    override suspend fun pin(targetId: Long, targetType: RateTargetType, pin: Boolean): Boolean {
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

    override suspend fun togglePin(targetId: Long, targetType: RateTargetType): Boolean {
        val local = db.listPinQueries.queryByTarget(targetId, targetType).executeAsOneOrNull()
        return if (local != null) {
            db.listPinQueries.deleteById(local.id)
            false
        } else {
            db.listPinQueries.insert(targetId, targetType)
            true
        }
    }

    override fun observePinExist(targetId: Long, targetType: RateTargetType): Flow<Boolean> {
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

    override fun paging(sort: RateSort): PagingSource<Long, PaginatedEntity> {
        fun query(
            limit: Long,
            offset: Long
        ) = when (sort.sortOption) {
            RateSortOption.NAME -> db.listPinQueries.querySortName(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )
            RateSortOption.PROGRESS -> db.listPinQueries.querySortProgress(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )
            RateSortOption.DATE_CREATED -> db.listPinQueries.querySortDateCreated(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )
            RateSortOption.DATE_UPDATED -> db.listPinQueries.querySortDateUpdated(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )
            RateSortOption.DATE_AIRED -> db.listPinQueries.querySortDateAired(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )
            RateSortOption.MY_SCORE -> db.listPinQueries.querySortScore(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )
            RateSortOption.RATING -> db.listPinQueries.querySortRating(
                sort.isDescending.long,
                limit,
                offset,
                ::pinPaginated
            )
            else -> throw java.lang.IllegalArgumentException("$sort is not supported yet")
        }


        return QueryPaging(
            countQuery = db.listPinQueries.queryCount(),
            transacter = db.listPinQueries,
            dispatcher = dispatchers.io,
            queryProvider = ::query
        )
    }
}