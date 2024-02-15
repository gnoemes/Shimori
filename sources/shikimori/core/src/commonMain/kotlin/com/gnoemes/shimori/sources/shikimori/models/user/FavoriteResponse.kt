package com.gnoemes.shimori.sources.shikimori.models.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FavoriteResponse(
        @SerialName("id") val id : Long,
        @SerialName("name") val name : String,
        @SerialName("russian") val nameRu : String?,
        @SerialName("image") val image : String,
        @SerialName("url") val url : String
)