package com.gnoemes.shimori.main

import com.gnoemes.shimori.model.rate.RateTargetType

internal sealed class MainAction {
    object Random : MainAction()
    data class ChangeRateType(val rateTargetType: RateTargetType) : MainAction()
}