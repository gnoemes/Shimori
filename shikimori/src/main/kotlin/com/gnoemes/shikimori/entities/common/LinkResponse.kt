package com.gnoemes.shikimori.entities.common

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class LinkResponse(
        @SerialName("id") val id: Long,
        @SerialName("kind") val name: String?,
        @SerialName("url") val url: String
)