package com.gnoemes.shimori.data.app

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType

data class ListPin(
    override val id: Long,
    val targetId: Long,
    val targetType: TrackTargetType
) : ShimoriEntity