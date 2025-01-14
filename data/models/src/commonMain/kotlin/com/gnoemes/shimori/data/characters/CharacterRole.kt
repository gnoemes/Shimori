package com.gnoemes.shimori.data.characters

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.track.TrackTargetType

data class CharacterRole(
    override val id: Long = 0,
    val characterId: Long,
    val targetId: Long,
    val targetType: TrackTargetType,
) : ShimoriEntity