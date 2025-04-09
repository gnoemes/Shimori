package com.gnoemes.shimori.source.shikimori.models.common

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class ImageResponse(
    @SerialName("original") val original: String?,
    @SerialName("preview") val preview: String?,
    @SerialName("x96") val x96: String?,
    @SerialName("x48") val x48: String?
)