package com.gnoemes.shimori.main

import com.gnoemes.shimori.model.rate.RateTargetType

internal sealed class MainAction {
    object Random : MainAction()
    object RateTypeDialog : MainAction()
    object RateTypeDialogClosed : MainAction()
    data class ChangeRateType(val rateTargetType: RateTargetType) : MainAction()
}