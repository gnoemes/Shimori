package com.gnoemes.shimori.lists_edit

internal sealed class ListEditInputState {
    object None : ListEditInputState()
    object Progress : ListEditInputState()
    object Rewatching : ListEditInputState()
    object Comment : ListEditInputState()
}