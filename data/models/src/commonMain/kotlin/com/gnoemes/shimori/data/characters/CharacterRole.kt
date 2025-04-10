package com.gnoemes.shimori.data.characters

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.track.TrackTargetType

@kotlinx.serialization.Serializable
data class CharacterRole(
    override val id: Long = 0,
    val characterId: Long,
    val targetId: Long,
    val targetType: TrackTargetType,
    val role: String? = null,
    val roleRu: String? = null,
) : ShimoriEntity