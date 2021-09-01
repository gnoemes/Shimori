package com.gnoemes.shimori.lists_change

import com.gnoemes.shimori.model.rate.ListType

internal sealed class ListsChangeAction {
    object Random : ListsChangeAction()
    data class ChangeListType(val listType: ListType) : ListsChangeAction()
}