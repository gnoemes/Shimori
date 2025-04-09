package com.gnoemes.shimori.source.track

import com.gnoemes.shimori.source.model.STrack
import com.gnoemes.shimori.source.model.SourceIdArgument

interface TrackDataSource {
    suspend fun getList(id: SourceIdArgument): List<STrack>
    suspend fun create(track: STrack): STrack
    suspend fun update(track: STrack): STrack
    suspend fun delete(id: SourceIdArgument)
}