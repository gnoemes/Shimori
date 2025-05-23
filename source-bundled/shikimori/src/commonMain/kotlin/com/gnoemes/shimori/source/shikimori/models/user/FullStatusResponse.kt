package com.gnoemes.shimori.source.shikimori.models.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FullStatusResponse(
    @SerialName("anime") val anime: List<StatusResponse>,
    @SerialName("manga") val manga: List<StatusResponse>
)