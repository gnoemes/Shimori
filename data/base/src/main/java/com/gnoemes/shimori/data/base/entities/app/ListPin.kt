package com.gnoemes.shimori.data.base.entities.app

import com.gnoemes.shimori.data.base.entities.ShimoriEntity
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType

data class ListPin(
    override val id: Long,
    val targetId: Long,
    val targetType: RateTargetType
) : ShimoriEntity