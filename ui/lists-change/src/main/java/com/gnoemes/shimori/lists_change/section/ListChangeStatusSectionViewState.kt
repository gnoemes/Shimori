package com.gnoemes.shimori.lists_change.section

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.model.rate.RateStatus

@Immutable
data class ListChangeStatusSectionViewState(
    val statuses : List<RateStatus> = emptyList(),
    val selectedStatus : RateStatus? = null
)