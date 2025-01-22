package com.gnoemes.shimori.source

interface AuthSource : Source {
    suspend fun signIn(): SourceAuthState?
    suspend fun signUp(): SourceAuthState?
    suspend fun refreshToken(): SourceAuthState?
    suspend fun logout()
    fun getState(): SourceAuthState?
    val isAuthorized get() = getState()?.isAuthorized == true
}