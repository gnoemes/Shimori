package com.gnoemes.shimori.lists

import androidx.compose.runtime.Immutable
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort

@Immutable
internal data class ListsViewState(
    val loading : Boolean = true,
    val authStatus: ShikimoriAuthState = ShikimoriAuthState.LOGGED_OUT,
    val type: ListType = ListType.Anime,
    val activeSort: RateSort = RateSort.defaultForType(type),
    val sorts: List<RateSortOption> = RateSortOption.priorityForType(type),
    val user: UserShort? = null,
    val pages: List<RateStatus> = emptyList(),
) {
    companion object {
        val Empty = ListsViewState()
    }
}