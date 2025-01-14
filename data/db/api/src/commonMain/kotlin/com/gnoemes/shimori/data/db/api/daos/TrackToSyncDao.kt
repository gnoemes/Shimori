package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.track.TrackToSync

interface TrackToSyncDao : EntityDao<TrackToSync> {

    fun queryAll(): List<TrackToSync>

    fun queryByTrackId(id: Long): List<TrackToSync>

    fun deleteBySourceId(sourceId: Long)
    fun deleteByTrackId(trackId: Long)

}