package com.gnoemes.shimori.source.shikimori.models.club

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class ClubCommentPolicy {
    @SerialName("free")
    FREE,
    @SerialName("members")
    MEMBERS,
}