package com.gnoemes.shimori.lists

import androidx.compose.runtime.Immutable
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.model.user.User

@Immutable
internal data class ListsViewState(
    val authStatus : ShikimoriAuthState = ShikimoriAuthState.LOGGED_OUT,
    val type : RateTargetType = RateTargetType.ANIME,
    val activeSort: RateSort = RateSort.defaultForType(type),
    val sorts: List<RateSortOption> = RateSortOption.priorityForType(type),
    val user : User? = null
) {
    companion object {
        val Empty = ListsViewState()
    }
}