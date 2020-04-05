package com.gnoemes.shimori.rates.edit

import com.airbnb.mvrx.MvRxState
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType

data class RateEditViewState(
    val name: String = "",
    val type: RateTargetType = RateTargetType.ANIME,
    val statuses: List<RateStatus> = RateStatus.watchingFirstValues(),
    val rate: Rate? = null,
    val rateShikimoriId: Long? = null,
    val shikimoriTargetId : Long? = null
) : MvRxState