package com.gnoemes.shikimori.entities.user

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class UserHistoryResponse(
    @SerialName("id") val id: Long,
    @SerialName("created_at") val dateCreated: Instant,
    @SerialName("description") val description: String
//        @SerialName("target") val target : LinkedContentResponse?
)