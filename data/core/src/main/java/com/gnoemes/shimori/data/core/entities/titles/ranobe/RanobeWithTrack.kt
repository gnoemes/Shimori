package com.gnoemes.shimori.data.core.entities.titles.ranobe

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.TitleWithTrack
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType

@kotlinx.serialization.Serializable
data class RanobeWithTrack(
    override val entity: Ranobe,
    override val track: Track?,
    override val pinned: Boolean
) : TitleWithTrack<Ranobe>, PaginatedEntity {
    override val id: Long = entity.id
    override val type: TrackTargetType = entity.type
}