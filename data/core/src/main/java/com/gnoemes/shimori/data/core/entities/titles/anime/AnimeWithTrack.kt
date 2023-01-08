package com.gnoemes.shimori.data.core.entities.titles.anime

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.TitleWithTrack
import com.gnoemes.shimori.data.core.entities.track.Track
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType

@kotlinx.serialization.Serializable
data class AnimeWithTrack(
    override val entity : Anime,
    override val track: Track?,
    override val pinned : Boolean
) : TitleWithTrack<Anime>, PaginatedEntity {
    override val id: Long = entity.id
    override val type: TrackTargetType = entity.type
}