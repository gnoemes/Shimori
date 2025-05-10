package com.gnoemes.shimori.data.common

import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.track.TrackTargetType


data class Related(
    override val id: Long,
    val targetId: Long,
    val targetType: TrackTargetType,
    val relationType: RelationType,
    val title: TitleWithTrackEntity
) : PaginatedEntity {
    val relatedId: Long get() = title.id
    val relatedType: TrackTargetType get() = title.type
}