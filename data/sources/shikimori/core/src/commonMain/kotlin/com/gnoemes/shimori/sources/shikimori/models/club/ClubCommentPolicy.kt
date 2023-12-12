package com.gnoemes.shimori.sources.shikimori.models.club

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class ClubCommentPolicy {
    @SerialName("free")
    FREE,
    @SerialName("members")
    MEMBERS,
}