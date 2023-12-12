package com.gnoemes.shimori.data.shikimori.models.user

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserDetailsResponse(
    @SerialName("id") val id: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("image") val image: UserImageResponse,
    @SerialName("last_online_at") val dateLastOnline: Instant?,
    @SerialName("name") val name: String?,
    @SerialName("sex") val sex: String?,
    @SerialName("website") val website: String?,
    @SerialName("birth_on") val dateBirth: String?,
    @SerialName("locale") val locale: String?,
    @SerialName("full_years") val fullYears: Int?,
    @SerialName("last_online") val lastOnline: String,
    @SerialName("location") val location: String?,
    @SerialName("banned") val isBanned: Boolean,
    @SerialName("about") val about: String?,
    @SerialName("common_info") val commonInfo: List<String>,
    @SerialName("show_comments") val isShowComments: Boolean,
    @SerialName("in_friends") val isFriend: Boolean,
    @SerialName("is_ignored") val isIgnored: Boolean,
    @SerialName("stats") val stats: UserStatsResponse
)