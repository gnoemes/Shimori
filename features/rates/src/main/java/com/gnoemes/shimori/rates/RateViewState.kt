package com.gnoemes.shimori.rates

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType

data class RateViewState(
    val type: RateTargetType = RateTargetType.ANIME,
    val query: String? = null,
    val selectedCategory: RateStatus? = null,
    val categories: List<RateCategory> = emptyList(),
    val sort: RateSortOption = RateSortOption.NAME,
    val isDescending: Boolean = false,
    val rates: Async<List<EntityWithRate<*>>> = Uninitialized,
    val authState: ShikimoriAuthState = ShikimoriAuthState.LOGGED_OUT,
    val isRefreshing: Boolean = false,
    val categoriesRefreshing: Boolean = false
) : MvRxState