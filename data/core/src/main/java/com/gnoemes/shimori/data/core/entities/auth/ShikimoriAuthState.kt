package com.gnoemes.shimori.data.core.entities.auth

enum class ShikimoriAuthState {
    LOGGED_IN,
    LOGGED_OUT;

    val isAuthorized : Boolean
        get() = this == LOGGED_IN
}