package com.gnoemes.common.epoxy

import com.airbnb.epoxy.EpoxyModel

object TotalSpanOverride : EpoxyModel.SpanSizeOverrideCallback {
    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = totalSpanCount
}