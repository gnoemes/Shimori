package com.gnoemes.shimori.lists.sort

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.core.entities.rate.RateSortOption

@Immutable
internal data class ListSortViewState(
    val listType: ListType = ListType.Anime,
    val activeSort: RateSort = RateSort.defaultForType(listType),
    val options: List<RateSortOption> = emptyList()
) {
    companion object {
        val Empty = ListSortViewState()
    }
}