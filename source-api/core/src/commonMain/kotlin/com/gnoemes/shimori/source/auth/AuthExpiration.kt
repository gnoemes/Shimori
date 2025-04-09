package com.gnoemes.shimori.source.auth

interface AuthExpiration {
    suspend fun onAuthExpired(sourceId: Long)
}