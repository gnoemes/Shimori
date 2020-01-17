package com.gnoemes.shimori.rates

import com.gnoemes.shimori.model.rate.RateStatus

data class RateCategory(
    val status: RateStatus,
    val count: Int
)