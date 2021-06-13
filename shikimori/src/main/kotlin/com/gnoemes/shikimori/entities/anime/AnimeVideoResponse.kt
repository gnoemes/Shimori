package com.gnoemes.shikimori.entities.anime

import com.google.gson.annotations.SerializedName

internal data class AnimeVideoResponse(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String?,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("image_url") val imageUrl: String?,
    @field:SerializedName("kind") private val _type: ShikimoriAnimeVideoType?,
    @field:SerializedName("hosting") val hosting: String?
) {
    val type: ShikimoriAnimeVideoType
        get() = _type ?: ShikimoriAnimeVideoType.OTHER
}