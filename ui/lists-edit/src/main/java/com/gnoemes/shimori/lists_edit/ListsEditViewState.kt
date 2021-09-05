package com.gnoemes.shimori.lists_edit

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.model.common.ShimoriImage
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType

@Immutable
internal data class ListsEditViewState(
    val image: ShimoriImage? = null,
    val name: String = "",
    val status: RateStatus = RateStatus.PLANNED,
    val progress: Int = 0,
    val size: Int? = null,
    val rewatches: Int = 0,
    val score: Int? = null,
    val comment: String? = null,
    val type: RateTargetType = RateTargetType.ANIME,
    val inputState : InputState = InputState.None,
    val pinned: Boolean = false,
    val newRate : Boolean = false,
) {

    companion object {
        val Empty = ListsEditViewState()
    }
}