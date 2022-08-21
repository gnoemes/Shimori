package com.gnoemes.shikimori.entities.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class FullStatusResponse(
        @SerialName("anime") val anime: List<StatusResponse>,
        @SerialName("manga") val manga: List<StatusResponse>
)