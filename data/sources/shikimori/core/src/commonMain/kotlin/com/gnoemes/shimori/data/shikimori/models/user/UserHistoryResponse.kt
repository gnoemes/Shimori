package com.gnoemes.shimori.data.shikimori.models.user

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserHistoryResponse(
    @SerialName("id") val id: Long,
    @SerialName("created_at") val dateCreated: Instant,
    @SerialName("description") val description: String
//        @SerialName("target") val target : LinkedContentResponse?
)