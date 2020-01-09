package com.gnoemes.shimori.rates

import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType

data class RateCategory(
    val targetType: RateTargetType,
    val status: RateStatus,
    val count: Int
)