package com.gnoemes.shimori.data.shikimori.models.user

import com.gnoemes.shimori.data.shikimori.models.common.StatisticResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class StatResponse(
    @SerialName("anime") val anime : List<StatisticResponse>,
    @SerialName("manga") val manga : List<StatisticResponse>?
)