package com.gnoemes.shikimori.entities.common

import com.google.gson.annotations.SerializedName

internal data class LinkResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("kind") val name: String?,
        @field:SerializedName("url") val url: String
)