package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.base.database.daos.RateDao
import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.rate
import com.gnoemes.shimori.data.shared.singleResult
import com.gnoemes.shimori.data.shared.syncerForEntity
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.system.measureTimeMillis

internal class RateDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
) : RateDao() {

    val syncer = syncerForEntity(
        this,
        { it.shikimoriId },
        { remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    override suspend fun insert(entity: Rate) {
        entity.let {
            db.rateQueries.insert(
                it.shikimoriId,
                it.targetId,
                it.targetType,
                it.status,
                it.score,
                it.comment,
                it.progress,
                it.reCounter,
                it.dateCreated,
                it.dateUpdated
            )
        }
    }

    override suspend fun deleteEntity(entity: Rate) {
        db.rateQueries.deleteById(entity.id)
    }

    override suspend fun syncAll(data: List<Rate>) {
        val time = measureTimeMillis {
            syncer.sync(
                currentValues = db.rateQueries.queryAll(::rate).executeAsList(),
                networkValues = data,
                removeNotMatched = true
            )
        }

        logger.i("Rate sync time $time mills")
    }

    override suspend fun queryById(id: Long): Rate? {
        return db.rateQueries
            .queryById(id, ::rate)
            .executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<Rate?> {
        return db.rateQueries
            .queryById(id, ::rate)
            .asFlow()
            .singleResult()
    }

    override fun observeByShikimoriId(id: Long): Flow<Rate?> {
        return db.rateQueries
            .queryByShikimoriId(id, ::rate)
            .asFlow()
            .singleResult()
    }

    override fun observeByTarget(targetId: Long, targetType: RateTargetType): Flow<Rate?> {
        return db.rateQueries
            .queryByTarget(targetId, targetType, ::rate)
            .asFlow()
            .singleResult()
    }

    override fun observeHasRates(): Flow<Boolean> {
        return db.rateQueries
            .queryCount()
            .asFlow()
            .map { it.executeAsOne() }
            .map { it > 0 }
    }
}