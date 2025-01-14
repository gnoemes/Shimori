package com.gnoemes.shimori.sources.shikimori.models.common

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GenreResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("russian") val nameRu: String?,
    @SerialName("kind") val type: String?
)