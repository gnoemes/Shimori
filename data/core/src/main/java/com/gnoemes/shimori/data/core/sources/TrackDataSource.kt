package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.user.UserShort

interface TrackDataSource {
    suspend fun getList(user: UserShort): List<Track>
    suspend fun create(track: Track): Track
    suspend fun update(track: Track): Track
    suspend fun delete(id: Long)
}