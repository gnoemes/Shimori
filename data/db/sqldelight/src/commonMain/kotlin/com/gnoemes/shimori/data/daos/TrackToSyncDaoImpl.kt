package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.db.api.daos.TrackToSyncDao
import com.gnoemes.shimori.data.track.TrackToSync
import com.gnoemes.shimori.data.util.trackToSyncMapper
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject

@Inject
class TrackToSyncDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : TrackToSyncDao, SqlDelightEntityDao<TrackToSync> {

    override fun insert(entity: TrackToSync): Long {
        entity.let {
            db.trackToSyncQueries.insert(
                it.trackId,
                it.action,
                it.attempts,
                it.lastAttempt,
            )
        }
        return db.animeScreenshotQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: TrackToSync) {
        db.trackToSyncQueries.update(trackToSyncMapper.map(entity))
    }

    override fun delete(entity: TrackToSync) {
        db.trackToSyncQueries.deleteById(entity.id)
    }

    override fun deleteByTrackId(trackId: Long) {
        db.trackToSyncQueries.deleteByTrackId(trackId)
    }

    override fun deleteBySourceId(sourceId: Long) {
        db.trackToSyncQueries.deleteBySourceId(sourceId)
    }

    override fun queryAll(): List<TrackToSync> {
        return db.trackToSyncQueries.queryAll()
            .executeAsList()
            .map { trackToSyncMapper.mapInverse(it) }
    }

    override fun queryByTrackId(id: Long): List<TrackToSync> {
        return db.trackToSyncQueries.queryByTrackId(id)
            .executeAsList()
            .map { trackToSyncMapper.mapInverse(it) }
    }
}