package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.track.TrackToSync

abstract class TrackToSyncDao : EntityDao<TrackToSync>() {

    abstract suspend fun queryAll(): List<TrackToSync>

    abstract suspend fun queryByTrackId(id: Long): List<TrackToSync>

    abstract suspend fun deleteBySourceId(sourceId: Long)
    abstract suspend fun deleteByTrackId(trackId: Long)

}