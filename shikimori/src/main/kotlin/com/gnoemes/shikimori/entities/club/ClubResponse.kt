package com.gnoemes.shikimori.entities.club

import com.gnoemes.shikimori.entities.common.ImageResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class ClubResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("logo") val image: ImageResponse,
    @SerialName("is_censored") val isCensored: Boolean,
    @SerialName("join_policy") val policyJoin: ClubPolicy?,
    @SerialName("comment_policy") val policyComment: ClubCommentPolicy?
)