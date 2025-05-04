package com.gnoemes.shimori.data.person

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.track.TrackTargetType

@kotlinx.serialization.Serializable
data class PersonRole(
    override val id: Long = 0,
    val personId: Long,
    val targetId: Long,
    val targetType: TrackTargetType,
    val role: String? = null,
    val roleRu: String? = null,
) : ShimoriEntity