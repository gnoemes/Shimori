package com.gnoemes.shimori.data.list

import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.rate.Rate

sealed class ListsUiEvents {
    object IncrementerHint : ListsUiEvents()

    data class IncrementerProgress(
        val title: TitleWithRateEntity,
        val oldRate: Rate,
        val newProgress: Int
    ) : ListsUiEvents()

    data class PinStatusChanged(
        val title: TitleWithRateEntity,
        val pinned : Boolean
    ) : ListsUiEvents()

    data class RateDeleted(
        val image: ShimoriImage?,
        val rate: Rate
    ) : ListsUiEvents()
}