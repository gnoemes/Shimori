package com.gnoemes.shimori.lists.page

import com.gnoemes.shimori.common.api.UiMessage
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort
import javax.annotation.concurrent.Immutable

@Immutable
internal data class ListPageViewState(
    val type: ListType = ListType.Anime,
    val status : RateStatus = RateStatus.WATCHING,
    val user: UserShort? = null,
    val message: UiMessage? = null,
) {
    companion object {
        val Empty = ListPageViewState()
    }
}