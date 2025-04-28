package com.gnoemes.shimori.source.shikimori.models.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudioResponse(
    val id: Long,
    val name: String,
    @SerialName("image_url") val imageUrl: String? = null,
)
