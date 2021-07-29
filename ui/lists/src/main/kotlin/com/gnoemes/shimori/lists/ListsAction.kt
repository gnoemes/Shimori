package com.gnoemes.shimori.lists

import com.gnoemes.shimori.model.rate.RateSortOption

internal sealed class ListsAction {
    object Refresh : ListsAction()

    data class UpdateListSort(val option: RateSortOption, val isDescending: Boolean) : ListsAction()
}