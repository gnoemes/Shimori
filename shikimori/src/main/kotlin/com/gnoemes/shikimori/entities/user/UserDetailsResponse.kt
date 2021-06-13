package com.gnoemes.shikimori.entities.user

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

internal data class UserDetailsResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("nickname") val nickname: String,
        @field:SerializedName("image") val image: UserImageResponse,
        @field:SerializedName("last_online_at") val dateLastOnline: DateTime,
        @field:SerializedName("name") val name: String?,
        @field:SerializedName("sex") val sex: String?,
        @field:SerializedName("website") val website: String?,
        @field:SerializedName("birth_on") val dateBirth: DateTime?,
        @field:SerializedName("locale") val locale: String?,
        @field:SerializedName("full_years") val fullYears: Int?,
        @field:SerializedName("last_online") val lastOnline: String,
        @field:SerializedName("location") val location: String?,
        @field:SerializedName("banned") val isBanned: Boolean,
        @field:SerializedName("about") val about: String?,
        @field:SerializedName("common_info") val commonInfo : List<String>,
        @field:SerializedName("show_comments") val isShowComments : Boolean,
        @field:SerializedName("in_friends") val isFriend : Boolean,
        @field:SerializedName("is_ignored") val isIgnored : Boolean,
        @field:SerializedName("stats") val stats : UserStatsResponse
)