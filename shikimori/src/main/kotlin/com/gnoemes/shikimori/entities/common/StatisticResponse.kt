package com.gnoemes.shikimori.entities.common

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class StatisticResponse(
        @SerialName("name") val name : String,
        @SerialName("value") val value : Int
)