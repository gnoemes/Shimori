package com.gnoemes.shimori.main

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.base.entities.ShikimoriAuthState
import com.gnoemes.shimori.model.rate.ListType

@Immutable
data class MainViewState(
    val listType: ListType = ListType.Anime,
    val hasRates : Boolean = false,
    val authState: ShikimoriAuthState = ShikimoriAuthState.LOGGED_OUT
) {

    companion object {
        val Empty = MainViewState()
    }
}