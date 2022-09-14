package com.gnoemes.shimori.lists.edit

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType

@Immutable
internal data class ListsEditViewState(
    val title : ShimoriTitleEntity? = null,
    val status: RateStatus = RateStatus.PLANNED,
    val progress: Int = 0,
    val rewatches: Int = 0,
    val score: Int? = null,
    val comment: String? = null,
    val type: RateTargetType = RateTargetType.ANIME,
    val inputState: ListEditInputState = ListEditInputState.None,
    val pinned: Boolean = false,
    val newRate: Boolean = false,
) {

    companion object {
        val Empty = ListsEditViewState()
    }
}

internal sealed class UiEvents {
    object NavigateUp : UiEvents()
}

internal sealed class ListEditInputState {
    object None : ListEditInputState()
    object Progress : ListEditInputState()
    object Rewatching : ListEditInputState()
    object Comment : ListEditInputState()
}