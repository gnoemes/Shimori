package com.gnoemes.shimori.data.core.entities.app

import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType

data class ListPin(
    override val id: Long,
    val targetId: Long,
    val targetType: TrackTargetType
) : ShimoriEntity