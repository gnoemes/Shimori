package com.gnoemes.shikimori.entities.user

import com.gnoemes.shikimori.entities.common.StatisticResponse
import com.google.gson.annotations.SerializedName

internal data class StatResponse(
    @field:SerializedName("anime") val anime : List<StatisticResponse>,
    @field:SerializedName("manga") val manga : List<StatisticResponse>?
)