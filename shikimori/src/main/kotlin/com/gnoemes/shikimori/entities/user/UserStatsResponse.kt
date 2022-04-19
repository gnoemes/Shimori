package com.gnoemes.shikimori.entities.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class UserStatsResponse(
        @SerialName("full_statuses") val status : FullStatusResponse,
        @SerialName("scores") val scores: StatResponse,
        @SerialName("types") val types: StatResponse,
        @SerialName("ratings") val ratings: StatResponse,
        @SerialName("has_anime?") val hasAnime: Boolean,
        @SerialName("has_manga?") val hasManga: Boolean
)