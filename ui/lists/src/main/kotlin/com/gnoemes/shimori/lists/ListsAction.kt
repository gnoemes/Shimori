package com.gnoemes.shimori.lists

import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateStatus

internal sealed class ListsAction {
    object Refresh : ListsAction()
    data class PageChanged(val newPage : RateStatus) : ListsAction()
    data class UpdateListSort(val option: RateSortOption, val isDescending: Boolean) : ListsAction()
}