package com.gnoemes.shikimori.entities.club

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal enum class ClubPolicy {
    @SerialName("free")
    FREE,
    @SerialName("admin_invite")
    ADMIN_INVITE,
    @SerialName("owner_invite")
    OWNER_INVITE
}