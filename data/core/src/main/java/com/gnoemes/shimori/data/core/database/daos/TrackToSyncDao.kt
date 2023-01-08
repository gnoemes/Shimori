package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.core.entities.track.TrackToSync

abstract class TrackToSyncDao : EntityDao<TrackToSync>() {

    abstract suspend fun queryAll(): List<TrackToSync>

    abstract suspend fun queryAllByTarget(target: SyncApi): List<TrackToSync>

    abstract suspend fun queryByTrackId(id : Long) : TrackToSync?

}