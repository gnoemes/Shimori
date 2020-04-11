package com.gnoemes.shimori.rates.sort

import com.airbnb.mvrx.MvRxState
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType

data class RateSortViewState(
    val type: RateTargetType = RateTargetType.ANIME,
    val items: List<RateSortOption> = RateSortOption.priorityValues(),
    val sort : RateSort = RateSort.default(),
    val selected: RateSortOption = sort.sortOption
) : MvRxState