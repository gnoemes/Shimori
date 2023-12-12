package com.gnoemes.shimori.data.shikimori.models.club

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class ClubPolicy {
    @SerialName("free")
    FREE,
    @SerialName("admin_invite")
    ADMIN_INVITE,
    @SerialName("owner_invite")
    OWNER_INVITE
}