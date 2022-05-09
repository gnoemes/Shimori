package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.RateSortDao
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.RateSortDAO
import com.gnoemes.shimori.data.shared.RateSortMapper
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RateSortDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
) : RateSortDao() {

    override suspend fun insert(entity: RateSort) {
        entity.let {
            db.rateSortQueries.insert(
                it.type.type,
                it.sortOption,
                it.isDescending
            )
        }
    }

    override suspend fun update(entity: RateSort) {
        entity.let {
            db.rateSortQueries.update(
                RateSortDAO(
                    it.id,
                    it.type.type,
                    it.sortOption,
                    it.isDescending
                )
            )
        }
    }

    override suspend fun delete(entity: RateSort) {
        db.rateSortQueries.deleteById(entity.id)
    }

    override suspend fun query(type: ListType): RateSort? {
        return db.rateSortQueries.queryByType(type.type).executeAsOneOrNull()?.let {
            RateSortMapper.map(it)
        }
    }

    override fun observe(type: ListType): Flow<RateSort?> {
        return db.rateSortQueries.queryByType(type.type)
            .asFlow()
            .mapToOneOrNull()
            .map(RateSortMapper::map)
    }
}