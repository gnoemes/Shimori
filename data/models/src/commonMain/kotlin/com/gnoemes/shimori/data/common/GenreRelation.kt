package com.gnoemes.shimori.data.common

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.track.TrackTargetType

@kotlinx.serialization.Serializable
data class GenreRelation(
    override val id: Long = 0,
    val sourceId: Long,
    val targetId : Long,
    val type : TrackTargetType,
    val ids : List<Long>
) : ShimoriEntity