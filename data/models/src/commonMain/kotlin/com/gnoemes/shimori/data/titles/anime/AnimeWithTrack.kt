package com.gnoemes.shimori.data.titles.anime

import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.TitleWithTrack
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType

@kotlinx.serialization.Serializable
data class AnimeWithTrack(
    override val entity: Anime,
    override val track: Track?,
    override val pinned: Boolean
) : TitleWithTrack<Anime>, PaginatedEntity {
    override val id: Long = entity.id
    override val type: TrackTargetType = entity.type
}