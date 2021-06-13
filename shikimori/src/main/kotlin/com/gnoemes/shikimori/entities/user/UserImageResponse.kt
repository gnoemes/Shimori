package com.gnoemes.shikimori.entities.user

import com.google.gson.annotations.SerializedName

internal data class UserImageResponse(
        @field:SerializedName("x160") val x160: String?,
        @field:SerializedName("x148") val x148: String?,
        @field:SerializedName("x80") val x80: String?,
        @field:SerializedName("x64") val x64: String?,
        @field:SerializedName("x48") val x48: String?,
        @field:SerializedName("x32") val x32: String?,
        @field:SerializedName("x16") val x16: String?
)