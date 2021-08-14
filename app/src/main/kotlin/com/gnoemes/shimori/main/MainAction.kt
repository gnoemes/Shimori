package com.gnoemes.shimori.main

import com.gnoemes.shimori.model.rate.ListType

internal sealed class MainAction {
    object Random : MainAction()
    data class ChangeListType(val listType: ListType) : MainAction()
}