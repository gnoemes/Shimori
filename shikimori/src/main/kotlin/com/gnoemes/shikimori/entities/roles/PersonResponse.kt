package com.gnoemes.shikimori.entities.roles

import com.gnoemes.shikimori.entities.common.ImageResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class PersonResponse(
        @SerialName("id") val id: Long,
        @SerialName("name") val name: String,
        @SerialName("russian") val nameRu: String?,
        @SerialName("image") val image: ImageResponse,
        @SerialName("url") val url: String
)