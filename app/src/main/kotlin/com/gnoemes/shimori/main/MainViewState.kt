package com.gnoemes.shimori.main

import androidx.compose.runtime.Immutable
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.model.rate.RateTargetType

@Immutable
data class MainViewState(
    val rateTargetType: RateTargetType = RateTargetType.ANIME,
    val authState: ShikimoriAuthState = ShikimoriAuthState.LOGGED_OUT
) {

    companion object {
        val Empty = MainViewState()
    }
}