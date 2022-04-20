package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.base.database.daos.RanobeDao
import com.gnoemes.shimori.data.base.entities.rate.RateSortOption
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.base.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.ranobe
import com.gnoemes.shimori.data.shared.ranobeDao
import com.gnoemes.shimori.data.shared.ranobeWithRate
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RanobeDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
) : RanobeDao() {

    override suspend fun insert(entity: Ranobe) {
        db.ranobeQueries.insert(ranobeDao(entity))
    }

    override suspend fun deleteEntity(entity: Ranobe) {
        db.ranobeQueries.deleteById(entity.id)
    }

    override suspend fun queryById(id: Long): Ranobe? {
        return db.ranobeQueries.queryById(id, ::ranobe)
            .executeAsOneOrNull()
    }

    override suspend fun queryAll(): List<Ranobe> {
        TODO("Not yet implemented")
    }

    override suspend fun queryAllWithStatus(): List<RanobeWithRate> {
        return db.ranobeQueries.queryAllWithStatus(::ranobeWithRate)
            .executeAsList()
    }

    override suspend fun queryByStatus(status: RateStatus): List<RanobeWithRate> {
        return db.ranobeQueries.queryByStatus(status, ::ranobeWithRate)
            .executeAsList()
    }

    override fun observeById(id: Long): Flow<RanobeWithRate?> {
        return db.mangaQueries.queryByIdWithRate(id, ::ranobeWithRate)
            .asFlow()
            .map { it.executeAsOneOrNull() }
    }

    override fun observeByStatus(status: RateStatus): Flow<List<RanobeWithRate>> {
        return db.ranobeQueries.queryByStatus(status, ::ranobeWithRate)
            .asFlow()
            .map { it.executeAsList() }
    }

    override fun paging(status: RateStatus, descending: Boolean, sortOption: RateSortOption) {
        TODO("Not yet implemented")
    }


}