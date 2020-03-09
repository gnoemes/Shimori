package com.gnoemes.shimori.rates

import com.gnoemes.shimori.model.rate.RateStatus

internal sealed class RateAction {
    data class ChangeCategory(val newCategory: RateStatus) : RateAction()
    object Refresh : RateAction()
    object ChangeOrder : RateAction()
}