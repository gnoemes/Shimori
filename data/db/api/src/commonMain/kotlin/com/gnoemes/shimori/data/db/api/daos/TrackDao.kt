package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

abstract class TrackDao : EntityDao<Track> {
    abstract fun observeById(id: Long): Flow<Track?>
    abstract fun observeByTarget(targetId: Long, targetType: TrackTargetType): Flow<Track?>
    abstract fun observeHasTracks(): Flow<Boolean>
    abstract fun observeExistedStatuses(type: TrackTargetType): Flow<List<TrackStatus>>

    abstract fun queryById(id: Long): Track?

    abstract fun queryAll(): List<Track>
}