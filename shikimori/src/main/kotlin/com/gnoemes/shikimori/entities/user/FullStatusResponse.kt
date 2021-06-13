package com.gnoemes.shikimori.entities.user

import com.google.gson.annotations.SerializedName

internal data class FullStatusResponse(
        @field:SerializedName("anime") val anime: List<StatusResponse>,
        @field:SerializedName("manga") val manga: List<StatusResponse>
)