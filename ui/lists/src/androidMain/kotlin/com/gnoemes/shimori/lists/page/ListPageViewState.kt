package com.gnoemes.shimori.lists.page

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.track.ListType

@Immutable
internal data class ListPageViewState(
    val type: ListType = ListType.Anime,
    val incrementerTitle: TitleWithTrackEntity? = null,
    val isLoading: Boolean = false,
) {
    companion object {
        val Empty = ListPageViewState()
    }
}

internal sealed class UiEvents {
    class EditTrack(val entity: TitleWithTrackEntity, val markComplete: Boolean) : UiEvents()
}