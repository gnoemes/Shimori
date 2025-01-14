package com.gnoemes.shimori.source.data

import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.user.UserShort

interface TrackDataSource {
    suspend fun getList(user: UserShort): List<Track>
    suspend fun create(track: Track): Track
    suspend fun update(track: Track): Track
    suspend fun delete(id: Long)
}