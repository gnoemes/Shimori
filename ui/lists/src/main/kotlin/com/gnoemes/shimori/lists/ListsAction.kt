package com.gnoemes.shimori.lists

import com.gnoemes.shimori.model.rate.ListsPage
import com.gnoemes.shimori.model.rate.RateSortOption

internal sealed class ListsAction {
    object Refresh : ListsAction()
    data class UpdateCurrentPage(val page : ListsPage) : ListsAction()
    data class UpdateListSort(val option: RateSortOption, val isDescending: Boolean) : ListsAction()
}