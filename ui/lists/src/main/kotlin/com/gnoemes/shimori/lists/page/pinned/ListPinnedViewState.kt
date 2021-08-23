package com.gnoemes.shimori.lists.page.pinned

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity

@Immutable
internal data class ListPinnedViewState(
    val list : List<EntityWithRate<out ShimoriEntity>> = emptyList()
)  {
    companion object {
        val Empty = ListPinnedViewState()
    }
}