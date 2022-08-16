package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.RateDao
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.*
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

internal class RateDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : RateDao() {

    private val syncer = syncerForEntity(
        this,
        { it.shikimoriId },
        { remote, local ->
            remote.copy(
                id = local?.id ?: 0,
                //do not override ranobe type
                targetType = local?.targetType ?: remote.targetType,
                targetId = getTargetId(remote, local)
            )
        },
        logger
    )

    private suspend fun getTargetId(remote: Rate, local: Rate?): Long {
        val targetType = local?.targetType ?: remote.targetType
        val targetId = local?.targetId?.takeIf { it > 0 }

        if (remote.targetShikimoriId == null) {
            return 0
        }

        if (targetId != null) {
            return targetId
        }

        return withContext(dispatchers.io) {
            (when (targetType) {
                RateTargetType.ANIME -> db
                    .animeQueries
                    .queryIdByShikimoriId(remote.targetShikimoriId!!)
                    .executeAsOneOrNull()
                RateTargetType.MANGA -> db
                    .mangaQueries
                    .queryIdByShikimoriId(remote.targetShikimoriId!!)
                    .executeAsOneOrNull()
                RateTargetType.RANOBE -> db
                    .ranobeQueries
                    .queryIdByShikimoriId(remote.targetShikimoriId!!)
                    .executeAsOneOrNull()
            })
            //mark for delete if we can't set target
                ?: 0
        }
    }

    override suspend fun insert(entity: Rate) {
        entity.let {
            db.rateQueries.insert(
                it.shikimoriId,
                it.targetId,
                it.targetType,
                it.targetShikimoriId,
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

    override suspend fun update(entity: Rate) {
        entity.let {
            db.rateQueries.update(
                RateDAO(
                    it.id,
                    it.shikimoriId,
                    it.targetId,
                    it.targetType,
                    it.targetShikimoriId,
                    it.status,
                    it.score,
                    it.comment,
                    it.progress,
                    it.reCounter,
                    it.dateCreated,
                    it.dateUpdated
                )
            )
        }
    }

    override suspend fun delete(entity: Rate) {
        db.rateQueries.deleteById(entity.id)
    }

    override suspend fun syncAll(data: List<Rate>) {
        val result: ItemSyncerResult<Rate>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.rateQueries.queryAll(::rate).executeAsList(),
                networkValues = data,
                removeNotMatched = true
            )
        }

        logger.i(
            "Rate sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override suspend fun syncAll(
        data: List<Rate>,
        target: RateTargetType,
        status: RateStatus?
    ) {
        val result: ItemSyncerResult<Rate>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues =
                if (status == null) db.rateQueries.queryAllByTarget(target, ::rate).executeAsList()
                else db.rateQueries.queryAllByTarget(target, ::rate).executeAsList(),
                networkValues = data,
                removeNotMatched = true
            )
        }

        logger.i(
            "Rate $target sync with status ${status ?: "All"} by results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
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
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeByShikimoriId(id: Long): Flow<Rate?> {
        return db.rateQueries
            .queryByShikimoriId(id, ::rate)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeByTarget(targetId: Long, targetType: RateTargetType): Flow<Rate?> {
        return db.rateQueries
            .queryByTarget(targetId, targetType, ::rate)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeHasRates(): Flow<Boolean> {
        return db.rateQueries
            .queryCount()
            .asFlow()
            .mapToOne(dispatchers.io)
            .map { it > 0 }
            .flowOn(dispatchers.io)
    }

    override fun observeExistedStatuses(type: RateTargetType): Flow<List<RateStatus>> {
        return combine(
            *RateStatus.listPagesOrder.map { status ->
                db.rateQueries.statusForTypeExist(type, status)
                    .asFlow()
                    .mapToOne(dispatchers.io)
                    .map { count -> status to (count > 0) }
            }
                .toTypedArray()
        ) { statuses ->
            statuses
                .filter { it.second }
                .map { it.first }
        }
            .flowOn(dispatchers.io)
    }
}