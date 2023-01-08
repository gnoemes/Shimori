package com.gnoemes.shimori.lists.change.section

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.track.TrackStatus

@Immutable
data class ListChangeStatusSectionViewState(
    val statuses : List<TrackStatus> = emptyList(),
    val selectedStatus : TrackStatus? = null
)