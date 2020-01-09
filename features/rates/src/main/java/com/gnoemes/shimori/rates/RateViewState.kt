package com.gnoemes.shimori.rates

import com.airbnb.mvrx.MvRxState
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType

data class RateViewState(
    val query: String? = null,
    val selectedStatus: RateStatus? = null,
    val sort: RateSortOption = RateSortOption.NAME,
    val isDescending: Boolean = false,
    val categories: List<RateCategory> = emptyList(),
    val type : RateTargetType = RateTargetType.ANIME,
    val rates: List<Rate>? = null,
    val authState: ShikimoriAuthState = ShikimoriAuthState.LOGGED_OUT,
    val isRefreshing: Boolean = false,
    val categoriesRefreshing : Boolean = false
) : MvRxState