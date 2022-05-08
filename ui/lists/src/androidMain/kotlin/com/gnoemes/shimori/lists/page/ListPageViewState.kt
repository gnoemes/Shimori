package com.gnoemes.shimori.lists.page

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.user.UserShort

@Immutable
internal data class ListPageViewState(
    val type: ListType = ListType.Anime,
    val status: RateStatus = RateStatus.WATCHING,
    val user: UserShort? = null,
    val message : UiMessage? = null,
) {
    companion object {
        val Empty = ListPageViewState()
    }
}