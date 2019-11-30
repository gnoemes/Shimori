package com.gnoemes.shikimori.entities.roles

import com.gnoemes.shikimori.entities.common.ImageResponse
import com.google.gson.annotations.SerializedName

internal data class CharacterResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("russian") val nameRu: String?,
        @field:SerializedName("image") val image: ImageResponse,
        @field:SerializedName("url") val url: String
)