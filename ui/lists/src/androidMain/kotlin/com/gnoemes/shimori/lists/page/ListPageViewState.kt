package com.gnoemes.shimori.lists.page

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity

@Immutable
internal data class ListPageViewState(
    val message: UiMessage? = null,
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