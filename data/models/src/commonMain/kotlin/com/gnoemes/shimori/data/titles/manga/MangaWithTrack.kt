package com.gnoemes.shimori.data.titles.manga

import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.TitleWithTrack
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType

@kotlinx.serialization.Serializable
data class MangaWithTrack(
    override val entity: Manga,
    override val track: Track?,
    override val pinned: Boolean
) : TitleWithTrack<Manga>, PaginatedEntity {
    override val id: Long = entity.id
    override val type: TrackTargetType = entity.type
}