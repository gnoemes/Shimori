package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.TrackDao
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
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

internal class TrackDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : TrackDao() {

    private val syncer = syncerForEntity(
        this,
        { it.shikimoriId },
        ::sync,
        logger
    )

    private suspend fun sync(remote: Track, local: Track?): Track {
        //if we have pending sync, we won't override local Track
        if (local != null) {
            val pendingSync = db
                .trackToSyncQueries
                .queryByTrackId(local.id)
                .executeAsOneOrNull()
                ?.let { trackToSyncMapper.mapInverse(it) }
            if (pendingSync != null) {
                return local
            }
        }

        return remote.copy(
            id = local?.id ?: 0,
            //do not override ranobe type
            targetType = local?.targetType ?: remote.targetType,
            targetId = getTargetId(remote, local)
        )
    }

    private suspend fun getTargetId(remote: Track, local: Track?): Long {
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
                TrackTargetType.ANIME -> db
                    .animeQueries
                    .queryIdByShikimoriId(remote.targetShikimoriId!!)
                    .executeAsOneOrNull()
                TrackTargetType.MANGA -> db
                    .mangaQueries
                    .queryIdByShikimoriId(remote.targetShikimoriId!!)
                    .executeAsOneOrNull()
                TrackTargetType.RANOBE -> db
                    .ranobeQueries
                    .queryIdByShikimoriId(remote.targetShikimoriId!!)
                    .executeAsOneOrNull()
            })
            //mark for delete if we can't set target
                ?: 0
        }
    }

    override suspend fun insert(entity: Track) {
        entity.let {
            db.trackQueries.insert(
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

    override suspend fun update(entity: Track) {
        entity.let {
            db.trackQueries.update(
                TrackDAO(
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

    override suspend fun delete(entity: Track) {
        db.trackQueries.deleteById(entity.id)
    }

    override suspend fun syncAll(data: List<Track>) {
        val result: ItemSyncerResult<Track>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues = db.trackQueries.queryAll(::track).executeAsList(),
                networkValues = data,
                removeNotMatched = true
            )
        }

        logger.i(
            "Track sync results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override suspend fun syncAll(
        data: List<Track>,
        target: TrackTargetType,
        status: TrackStatus?
    ) {
        val result: ItemSyncerResult<Track>
        val time = measureTimeMillis {
            result = syncer.sync(
                currentValues =
                if (status == null) db.trackQueries.queryAllByTarget(target, ::track).executeAsList()
                else db.trackQueries.queryAllByTarget(target, ::track).executeAsList(),
                networkValues = data,
                removeNotMatched = true
            )
        }

        logger.i(
            "Track $target sync with status ${status ?: "All"} by results --> Added: ${result.added.size} Updated: ${result.updated.size}, time: $time mills",
            tag = SYNCER_RESULT_TAG
        )
    }

    override suspend fun queryById(id: Long): Track? {
        return db.trackQueries
            .queryById(id, ::track)
            .executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<Track?> {
        return db.trackQueries
            .queryById(id, ::track)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeByShikimoriId(id: Long): Flow<Track?> {
        return db.trackQueries
            .queryByShikimoriId(id, ::track)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeByTarget(targetId: Long, targetType: TrackTargetType): Flow<Track?> {
        return db.trackQueries
            .queryByTarget(targetId, targetType, ::track)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeHasTracks(): Flow<Boolean> {
        return db.trackQueries
            .queryCount()
            .asFlow()
            .mapToOne(dispatchers.io)
            .map { it > 0 }
            .flowOn(dispatchers.io)
    }

    override fun observeExistedStatuses(type: TrackTargetType): Flow<List<TrackStatus>> {
        return combine(
            *TrackStatus.listPagesOrder.map { status ->
                db.trackQueries.statusForTypeExist(type, status)
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