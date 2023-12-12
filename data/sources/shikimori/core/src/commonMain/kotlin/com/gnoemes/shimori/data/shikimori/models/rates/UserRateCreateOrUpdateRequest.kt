package com.gnoemes.shimori.data.shikimori.models.rates

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserRateCreateOrUpdateRequest(
        @SerialName("user_rate") val userRate: UserRateResponse
)