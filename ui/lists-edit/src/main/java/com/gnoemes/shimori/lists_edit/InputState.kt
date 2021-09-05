package com.gnoemes.shimori.lists_edit

internal sealed class InputState {
    object None : InputState()
    object Progress : InputState()
    object Rewatching : InputState()
    object Comment : InputState()
}