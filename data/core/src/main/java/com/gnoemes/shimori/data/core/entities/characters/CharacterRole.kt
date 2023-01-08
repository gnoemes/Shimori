package com.gnoemes.shimori.data.core.entities.characters

import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType

data class CharacterRole(
    override val id: Long = 0,
    val characterId: Long,
    val targetId: Long,
    val targetType: TrackTargetType,
) : ShimoriEntity