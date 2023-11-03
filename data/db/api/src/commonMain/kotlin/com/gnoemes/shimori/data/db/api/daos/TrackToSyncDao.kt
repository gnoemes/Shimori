package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.track.TrackToSync

interface TrackToSyncDao : EntityDao<TrackToSync> {

    suspend fun queryAll(): List<TrackToSync>

    suspend fun queryByTrackId(id: Long): List<TrackToSync>

    suspend fun deleteBySourceId(sourceId: Long)
    suspend fun deleteByTrackId(trackId: Long)

}