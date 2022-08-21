package com.gnoemes.shikimori.entities.anime

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
internal data class AnimeVideoResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String?,
    @SerialName("url") val url: String,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("kind") private val _type: ShikimoriAnimeVideoType?,
    @SerialName("hosting") val hosting: String?
) {
    val type: ShikimoriAnimeVideoType
        get() = _type ?: ShikimoriAnimeVideoType.OTHER
}