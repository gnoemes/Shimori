package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.TrackToSyncDao
import com.gnoemes.shimori.data.core.entities.track.TrackToSync
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.trackToSyncMapper

internal class TrackToSyncDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : TrackToSyncDao() {

    override suspend fun insert(entity: TrackToSync) {
        entity.let {
            db.trackToSyncQueries.insert(
                it.trackId,
                it.action,
                it.attempts,
                it.lastAttempt,
            )
        }
    }

    override suspend fun update(entity: TrackToSync) {
        db.trackToSyncQueries.update(trackToSyncMapper.map(entity))
    }

    override suspend fun delete(entity: TrackToSync) {
        db.trackToSyncQueries.deleteById(entity.id)
    }

    override suspend fun deleteByTrackId(trackId: Long) {
        db.trackToSyncQueries.deleteByTrackId(trackId)
    }

    override suspend fun deleteBySourceId(sourceId: Long) {
        db.trackToSyncQueries.deleteBySourceId(sourceId)
    }



    override suspend fun queryAll(): List<TrackToSync> {
        return db.trackToSyncQueries.queryAll()
            .executeAsList()
            .map { trackToSyncMapper.mapInverse(it) }
    }

    override suspend fun queryByTrackId(id: Long): List<TrackToSync> {
        return db.trackToSyncQueries.queryByTrackId(id)
            .executeAsList()
            .map { trackToSyncMapper.mapInverse(it) }
    }
}