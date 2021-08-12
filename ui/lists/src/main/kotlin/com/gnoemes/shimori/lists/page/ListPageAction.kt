package com.gnoemes.shimori.lists.page

internal sealed class ListPageAction {
    data class TogglePin(val id : Long) : ListPageAction()
}