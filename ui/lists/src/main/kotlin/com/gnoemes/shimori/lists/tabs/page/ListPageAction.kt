package com.gnoemes.shimori.lists.tabs.page

import com.gnoemes.shimori.model.rate.RateTargetType

internal sealed class ListPageAction {
    data class TogglePin(val id : Long, val targetType : RateTargetType) : ListPageAction()
}