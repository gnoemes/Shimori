package com.gnoemes.shikimori.entities.anime

import com.google.gson.annotations.SerializedName

internal data class ScreenshotResponse(
        @field:SerializedName("original") val original: String?,
        @field:SerializedName("preview") val preview: String?
)