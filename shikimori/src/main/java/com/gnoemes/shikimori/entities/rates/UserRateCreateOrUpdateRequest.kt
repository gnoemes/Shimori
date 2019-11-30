package com.gnoemes.shikimori.entities.rates

import com.google.gson.annotations.SerializedName

internal data class UserRateCreateOrUpdateRequest(
        @field:SerializedName("user_rate") val userRate: UserRateResponse
)