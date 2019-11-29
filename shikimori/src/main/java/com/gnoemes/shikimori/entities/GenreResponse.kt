package com.gnoemes.shikimori.entities

import com.google.gson.annotations.SerializedName

internal data class GenreResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("name") val name: String,
        @field:SerializedName("russian") val nameRu: String?,
        @field:SerializedName("kind") val type: String?
)