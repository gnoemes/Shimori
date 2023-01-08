package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.TrackToSyncDao
import com.gnoemes.shimori.data.core.entities.app.SyncApi
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
                it.targets,
                it.action,
                it.attempts,
                it.lastAttempt
            )
        }
    }

    override suspend fun update(entity: TrackToSync) {
        db.trackToSyncQueries.update(trackToSyncMapper.map(entity))
    }

    override suspend fun delete(entity: TrackToSync) {
        db.trackToSyncQueries.deleteById(entity.id)
    }

    override suspend fun queryAll(): List<TrackToSync> {
        return db.trackToSyncQueries.queryAll()
            .executeAsList()
            .map { trackToSyncMapper.mapInverse(it) }

    }

    override suspend fun queryAllByTarget(target: SyncApi): List<TrackToSync> {
        return db.trackToSyncQueries.queryAllByTarget(target.name)
            .executeAsList()
            .map { trackToSyncMapper.mapInverse(it) }
    }


    override suspend fun queryByTrackId(id: Long): TrackToSync? {
        return db.trackToSyncQueries.queryByTrackId(id)
            .executeAsOneOrNull()
            ?.let { trackToSyncMapper.mapInverse(it) }
    }
}