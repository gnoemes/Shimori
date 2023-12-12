package com.gnoemes.shimori.data

interface SourceAuthState {
    val accessToken: String
    val refreshToken: String
    val isAuthorized: Boolean

    fun serializeToJson(): String

    companion object {
        val Empty = object : SourceAuthState {
            override val accessToken: String = ""
            override val refreshToken: String = ""
            override val isAuthorized: Boolean = false
            override fun serializeToJson(): String = "{}"
        }
    }
}

data class SimpleAuthState(
    override val accessToken: String,
    override val refreshToken: String
) : SourceAuthState {
    override val isAuthorized: Boolean
        get() = accessToken.isNotEmpty() && refreshToken.isNotEmpty()

    override fun serializeToJson(): String = error("serializeToJson not implemented")
}