package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.app.SourceDataType
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

abstract class TrackDao : SourceSyncEntityDao<Track>(SourceDataType.Track) {
    abstract suspend fun insert(track: Track): Long
    abstract suspend fun update(track: Track)
    abstract suspend fun delete(track: Track)

    suspend fun insertOrUpdate(track: Track): Long {
        return if (track.id == 0L) insert(track)
        else {
            update(track)
            track.id
        }
    }

    abstract suspend fun sync(sourceId: Long, remote: List<Track>)
    abstract suspend fun sync(
        sourceId: Long,
        target: TrackTargetType,
        status: TrackStatus?,
        remote: List<Track>,
    )

    abstract fun observeById(id: Long): Flow<Track?>
    abstract fun observeByTarget(targetId: Long, targetType: TrackTargetType): Flow<Track?>
    abstract fun observeHasTracks(): Flow<Boolean>
    abstract fun observeExistedStatuses(type: TrackTargetType): Flow<List<TrackStatus>>


    abstract suspend fun queryById(id: Long): Track?
}