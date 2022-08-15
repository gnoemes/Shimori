package com.gnoemes.shimori.lists

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.user.UserShort

@Immutable
internal data class ListsViewState(
    val type: ListType = ListType.Anime,
    val user: UserShort? = null,
    val isEmpty: Boolean = false,
    val hasRates: Boolean = true,
    val isLoading: Boolean = false
) {
    companion object {
        val Empty = ListsViewState()
    }
}

internal const val INCREMENTATOR_MAX_PROGRESS = 50