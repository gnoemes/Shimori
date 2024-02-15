package com.gnoemes.shimori.sources.shikimori.models.user

import com.gnoemes.shimori.sources.shikimori.models.common.StatisticResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class StatResponse(
    @SerialName("anime") val anime : List<StatisticResponse>,
    @SerialName("manga") val manga : List<StatisticResponse>?
)