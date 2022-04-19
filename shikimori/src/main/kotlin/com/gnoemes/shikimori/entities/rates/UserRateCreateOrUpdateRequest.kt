package com.gnoemes.shikimori.entities.rates

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class UserRateCreateOrUpdateRequest(
        @SerialName("user_rate") val userRate: UserRateResponse
)