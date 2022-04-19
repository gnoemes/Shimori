package com.gnoemes.shikimori.entities.user

import com.gnoemes.shikimori.entities.common.StatisticResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class StatResponse(
    @SerialName("anime") val anime : List<StatisticResponse>,
    @SerialName("manga") val manga : List<StatisticResponse>?
)