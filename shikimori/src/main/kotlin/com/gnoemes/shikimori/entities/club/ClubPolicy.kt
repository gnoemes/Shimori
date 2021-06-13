package com.gnoemes.shikimori.entities.club

import com.google.gson.annotations.SerializedName

internal enum class ClubPolicy {
    @SerializedName("free")
    FREE,
    @SerializedName("admin_invite")
    ADMIN_INVITE,
    @SerializedName("owner_invite")
    OWNER_INVITE
}