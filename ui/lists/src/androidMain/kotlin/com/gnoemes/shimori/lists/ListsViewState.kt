package com.gnoemes.shimori.lists

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.user.UserShort

@Immutable
internal data class ListsViewState(
    val type: ListType = ListType.Anime,
    val status: TrackStatus = TrackStatus.WATCHING,
    val user: UserShort? = null,
    val isEmpty: Boolean = false,
    val hasTracks: Boolean = false,
    val isLoading: Boolean = false,
    val message: UiMessage? = null,
) {
    companion object {
        val Empty = ListsViewState()
    }
}

internal const val INCREMENTATOR_MAX_PROGRESS = 50