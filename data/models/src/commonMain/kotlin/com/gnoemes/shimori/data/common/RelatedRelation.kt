package com.gnoemes.shimori.data.common

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.track.TrackTargetType

@kotlinx.serialization.Serializable
data class RelatedRelation(
    override val id: Long = 0,
    val targetId: Long,
    val targetType: TrackTargetType,
    val type: RelationType,
    val relation: String?,
    val relatedId: Long,
    val relatedType: TrackTargetType,
) : ShimoriEntity