package com.gnoemes.shikimori.entities.user

import com.google.gson.annotations.SerializedName
import org.threeten.bp.OffsetDateTime

internal data class UserBriefResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("nickname") val nickname: String,
        @field:SerializedName("avatar") val avatar: String?,
        @field:SerializedName("image") val image: UserImageResponse,
        @field:SerializedName("last_online_at") val dateLastOnline: OffsetDateTime,
        @field:SerializedName("name") val name: String?,
        @field:SerializedName("sex") val sex: String?,
        @field:SerializedName("website") val website: String?,
        @field:SerializedName("birth_on") val dateBirth: OffsetDateTime?,
        @field:SerializedName("locale") val locale: String?
)