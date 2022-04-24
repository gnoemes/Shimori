package com.gnoemes.shikimori.entities.user

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class UserBriefResponse(
    @SerialName("id") val id: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("avatar") val avatar: String?,
    @SerialName("image") val image: UserImageResponse,
    @SerialName("last_online_at") val dateLastOnline: Instant?,
    @SerialName("name") val name: String?,
    @SerialName("sex") val sex: String?,
    @SerialName("website") val website: String?,
    @SerialName("birth_on") val dateBirth: String?,
    @SerialName("locale") val locale: String?
)