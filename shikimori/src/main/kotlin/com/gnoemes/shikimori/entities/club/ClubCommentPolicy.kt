package com.gnoemes.shikimori.entities.club

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal enum class ClubCommentPolicy {
    @SerialName("free")
    FREE,
    @SerialName("members")
    MEMBERS,
}