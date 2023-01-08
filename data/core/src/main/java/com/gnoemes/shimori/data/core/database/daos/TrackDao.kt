package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

abstract class TrackDao : EntityDao<Track>() {
    abstract suspend fun syncAll(data: List<Track>)

    abstract suspend fun syncAll(data: List<Track>, target: TrackTargetType, status: TrackStatus?)

    abstract fun observeById(id: Long): Flow<Track?>
    abstract fun observeByShikimoriId(id: Long): Flow<Track?>
    abstract fun observeByTarget(targetId: Long, targetType: TrackTargetType): Flow<Track?>
    abstract fun observeHasTracks(): Flow<Boolean>
    abstract fun observeExistedStatuses(type: TrackTargetType): Flow<List<TrackStatus>>

    abstract suspend fun queryById(id: Long): Track?
}