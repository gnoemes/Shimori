package com.gnoemes.shimori.sources.shikimori.models.common

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class StatisticResponse(
        @SerialName("name") val name : String,
        @SerialName("value") val value : Int
)