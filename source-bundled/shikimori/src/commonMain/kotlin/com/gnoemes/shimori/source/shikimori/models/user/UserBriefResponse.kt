package com.gnoemes.shimori.source.shikimori.models.user

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserBriefResponse(
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