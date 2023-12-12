package com.gnoemes.shimori.data.shikimori.models.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserImageResponse(
        @SerialName("x160") val x160: String?,
        @SerialName("x148") val x148: String?,
        @SerialName("x80") val x80: String?,
        @SerialName("x64") val x64: String?,
        @SerialName("x48") val x48: String?,
        @SerialName("x32") val x32: String?,
        @SerialName("x16") val x16: String?
)