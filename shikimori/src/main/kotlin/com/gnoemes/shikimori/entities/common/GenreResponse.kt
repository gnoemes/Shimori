package com.gnoemes.shikimori.entities.common

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class GenreResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("russian") val nameRu: String?,
    @SerialName("kind") val type: String?
)