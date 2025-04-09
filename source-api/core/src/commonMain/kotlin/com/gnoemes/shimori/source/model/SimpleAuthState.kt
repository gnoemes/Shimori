package com.gnoemes.shimori.source.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class SimpleAuthState(
    override val sourceId: Long,
    override val accessToken: String,
    override val refreshToken: String
) : SourceAuthState {
    override val isAuthorized: Boolean
        get() = accessToken.isNotEmpty() && refreshToken.isNotEmpty()

    override fun serializeToJson(): String = Json.Default.encodeToString(this)
}