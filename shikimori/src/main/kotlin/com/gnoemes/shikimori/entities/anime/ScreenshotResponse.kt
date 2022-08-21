package com.gnoemes.shikimori.entities.anime

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class ScreenshotResponse(
        @SerialName("original") val original: String?,
        @SerialName("preview") val preview: String?
)