package com.gnoemes.shikimori.entities.user

enum class ShikimoriAuthState {
    LOGGED_IN,
    LOGGED_OUT;

    val isAuthorized : Boolean
        get() = this == LOGGED_IN
}