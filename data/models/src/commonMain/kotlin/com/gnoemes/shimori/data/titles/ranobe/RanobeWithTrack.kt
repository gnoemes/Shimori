package com.gnoemes.shimori.data.titles.ranobe

import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.TitleWithTrack
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType

@kotlinx.serialization.Serializable
data class RanobeWithTrack(
    override val entity: Ranobe,
    override val track: Track?,
    override val pinned: Boolean
) : TitleWithTrack<Ranobe>, PaginatedEntity {
    override val id: Long = entity.id
    override val type: TrackTargetType = entity.type
}