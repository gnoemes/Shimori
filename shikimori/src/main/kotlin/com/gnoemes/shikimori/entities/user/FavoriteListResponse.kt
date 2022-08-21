package com.gnoemes.shikimori.entities.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class FavoriteListResponse(
        @SerialName("animes") val animes: List<FavoriteResponse>,
        @SerialName("mangas") val mangas: List<FavoriteResponse>,
        @SerialName("characters") val characters: List<FavoriteResponse>,
        @SerialName("people") val people: List<FavoriteResponse>,
        @SerialName("mangakas") val mangakas: List<FavoriteResponse>,
        @SerialName("seyu") val seyu: List<FavoriteResponse>,
        @SerialName("producers") val producers: List<FavoriteResponse>
)