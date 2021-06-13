package com.gnoemes.shikimori.entities.club

import com.google.gson.annotations.SerializedName

internal enum class ClubCommentPolicy {
    @SerializedName("free")
    FREE,
    @SerializedName("members")
    MEMBERS,
}