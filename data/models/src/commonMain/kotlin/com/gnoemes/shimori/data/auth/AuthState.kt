package com.gnoemes.shimori.data.auth

enum class AuthState {
    LOGGED_IN,
    LOGGED_OUT;

    val isAuthorized : Boolean
        get() = this == LOGGED_IN
}