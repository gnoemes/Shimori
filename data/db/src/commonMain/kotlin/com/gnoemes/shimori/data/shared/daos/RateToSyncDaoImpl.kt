package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.RateToSyncDao
import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.core.entities.rate.RateToSync
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.rateToSyncMapper

internal class RateToSyncDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : RateToSyncDao() {

    override suspend fun insert(entity: RateToSync) {
        entity.let {
            db.rateToSyncQueries.insert(
                it.rateId,
                it.targets,
                it.action,
                it.attempts,
                it.lastAttempt
            )
        }
    }

    override suspend fun update(entity: RateToSync) {
        db.rateToSyncQueries.update(rateToSyncMapper.map(entity))
    }

    override suspend fun delete(entity: RateToSync) {
        db.rateToSyncQueries.deleteById(entity.id)
    }

    override suspend fun queryAll(): List<RateToSync> {
        return db.rateToSyncQueries.queryAll()
            .executeAsList()
            .map { rateToSyncMapper.mapInverse(it) }

    }

    override suspend fun queryAllByTarget(target: SyncApi): List<RateToSync> {
        return db.rateToSyncQueries.queryAllByTarget(target.name)
            .executeAsList()
            .map { rateToSyncMapper.mapInverse(it) }
    }


    override suspend fun queryByRateId(id: Long): RateToSync? {
        return db.rateToSyncQueries.queryByRateId(id)
            .executeAsOneOrNull()
            ?.let { rateToSyncMapper.mapInverse(it) }
    }
}