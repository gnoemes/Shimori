package com.gnoemes.shimori.lists.change.section

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.rate.RateStatus

@Immutable
data class ListChangeStatusSectionViewState(
    val statuses : List<RateStatus> = emptyList(),
    val selectedStatus : RateStatus? = null
)