package com.gnoemes.shimori.lists.page

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.rate.ListType

@Immutable
internal data class ListPageViewState(
    val type : ListType = ListType.Anime,
    val incrementerTitle: TitleWithRateEntity? = null,
    val isLoading : Boolean = false,
) {
    companion object {
        val Empty = ListPageViewState()
    }
}

internal sealed class UiEvents {
    class EditRate(val entity: TitleWithRateEntity) : UiEvents()
}