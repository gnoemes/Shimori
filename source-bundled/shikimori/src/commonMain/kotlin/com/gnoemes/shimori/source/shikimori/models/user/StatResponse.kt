package com.gnoemes.shimori.source.shikimori.models.user

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class StatResponse(
    @SerialName("anime") val anime : List<com.gnoemes.shimori.source.shikimori.models.common.StatisticResponse>,
    @SerialName("manga") val manga : List<com.gnoemes.shimori.source.shikimori.models.common.StatisticResponse>?
)