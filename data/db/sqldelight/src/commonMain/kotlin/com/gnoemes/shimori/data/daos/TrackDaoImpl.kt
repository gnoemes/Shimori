package com.gnoemes.shimori.data.daos


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.db.api.daos.TrackDao
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.SYNCER_RESULT_TAG
import com.gnoemes.shimori.data.util.syncRemoteIds
import com.gnoemes.shimori.data.util.syncerForEntity
import com.gnoemes.shimori.data.util.track
import com.gnoemes.shimori.data.util.trackToSyncMapper
import com.gnoemes.shimori.data.util.withTransaction
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject


@Inject
class TrackDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : TrackDao() {

    private val syncer = syncerForEntity(
        this,
        { _, track -> track.targetId to track.targetType },
        ::sync,
        logger,
        networkEntityToKey = ::findKey
    )

    private fun findKey(
        sourceId: Long,
        remote: Track,
    ): Pair<Long, TrackTargetType>? {
        val remoteTargetType = remote.targetType
        val (localTargetId, isRanobe) = db.sourceIdsSyncQueries
            .findLocalId(sourceId, remote.targetId, remoteTargetType.sourceDataType.type)
            .executeAsOneOrNull()
            .let { it to false }
            .let {
                //Check ranobe. On shikimori there's no difference between manga and ranobe in tracks
                if (it.first == null && remoteTargetType.manga) db.sourceIdsSyncQueries
                    .findLocalId(sourceId, remote.targetId, SourceDataType.Ranobe.type)
                    .executeAsOneOrNull() to true
                else it
            }

        //we can't create track for unknown target
        if (localTargetId == null) {
            return null
        }

        //update track with same key
        return localTargetId to when (remoteTargetType) {
            TrackTargetType.ANIME -> remoteTargetType
            TrackTargetType.MANGA -> if (isRanobe) TrackTargetType.RANOBE else remoteTargetType
            else -> remoteTargetType
        }
    }

    private suspend fun sync(sourceId: Long, remote: Track, local: Track?): Track {
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
            //do not override ranobe type
            targetType = local?.targetType ?: remote.targetType,
            targetId = getTargetId(sourceId, remote, local)
        )
    }

    private suspend fun getTargetId(sourceId: Long, remote: Track, local: Track?): Long {
        val targetId = local?.targetId?.takeIf { it > 0 }

        if (targetId != null) {
            return targetId
        }

        val remoteTargetType = remote.targetType

        return db.sourceIdsSyncQueries
            .findLocalId(sourceId, remote.targetId, remoteTargetType.sourceDataType.type)
            .executeAsOneOrNull()
            .let {
                //Check ranobe. On shikimori there's no difference between manga and ranobe in tracks
                if (it == null && remoteTargetType.manga) db.sourceIdsSyncQueries
                    .findLocalId(sourceId, remote.targetId, SourceDataType.Ranobe.type)
                    .executeAsOneOrNull()
                else it
            } ?: 0
    }

    override suspend fun insert(sourceId: Long, remote: Track) {
        db.withTransaction {
            remote.let {
                trackQueries.insert(
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
                val localId = trackQueries.selectLastInsertedRowId().executeAsOne()
                syncRemoteIds(sourceId, localId, remote.id, syncDataType)
            }
        }
    }

    override suspend fun insert(track: Track): Long {
        return track.let {
            db.trackQueries.insert(
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
            db.trackQueries.selectLastInsertedRowId().executeAsOne()
        }
    }

    override suspend fun update(sourceId: Long, remote: Track, local: Track) {
        db.withTransaction {
            remote.let {
                trackQueries.update(
                    comgnoemesshimoridatadb.data.Track(
                        local.id,
                        local.targetId,
                        local.targetType,
                        it.status,
                        it.score,
                        it.comment,
                        it.progress,
                        it.reCounter,
                        it.dateCreated,
                        it.dateUpdated
                    )
                )

                syncRemoteIds(sourceId, local.id, remote.id, syncDataType)
            }
        }
    }

    override suspend fun update(track: Track) {
        track.let {
            db.trackQueries.update(
                comgnoemesshimoridatadb.data.Track(
                    it.id,
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
            )
        }
    }

    override suspend fun delete(sourceId: Long, local: Track) {
        db.withTransaction {
            trackQueries.deleteById(local.id)
            sourceIdsSyncQueries.deleteByLocal(local.id, syncDataType.type)
        }
    }

    override suspend fun delete(track: Track) {
        db.trackQueries.deleteById(track.id)
    }

    override suspend fun sync(sourceId: Long, remote: List<Track>) {
        val result = syncer.sync(
            sourceId = sourceId,
            currentValues = db.trackQueries.queryAll(::track).executeAsList(),
            networkValues = remote,
            removeNotMatched = true
        )

        logger.i(tag = SYNCER_RESULT_TAG) {
            "Track sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }

    override suspend fun sync(
        sourceId: Long,
        target: TrackTargetType,
        status: TrackStatus?,
        remote: List<Track>
    ) {
        val result = syncer.sync(
            sourceId = sourceId,
            currentValues =
            if (status == null) db.trackQueries.queryAllByTarget(target, ::track)
                .executeAsList()
            else db.trackQueries.queryAllByTargetAndStatus(target, status, ::track)
                .executeAsList(),
            networkValues = remote,
            removeNotMatched = true
        )

        logger.i(tag = SYNCER_RESULT_TAG) {
            "Track $target sync with status ${status ?: "All"} by results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
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