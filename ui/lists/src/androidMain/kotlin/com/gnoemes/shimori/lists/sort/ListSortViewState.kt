package com.gnoemes.shimori.lists.sort

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.ListType

@Immutable
internal data class ListSortViewState(
    val listType: ListType = ListType.Anime,
    val activeSort: ListSort = ListSort.defaultForType(listType),
    val options: List<ListSortOption> = emptyList()
) {
    companion object {
        val Empty = ListSortViewState()
    }
}