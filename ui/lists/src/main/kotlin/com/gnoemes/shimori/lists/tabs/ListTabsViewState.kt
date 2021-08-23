package com.gnoemes.shimori.lists.tabs

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType

@Immutable
data class ListTabsViewState(
    val type: RateTargetType = RateTargetType.ANIME,
    val pages: List<RateStatus> = emptyList(),
) {
    companion object {
        val Empty = ListTabsViewState()
    }
}