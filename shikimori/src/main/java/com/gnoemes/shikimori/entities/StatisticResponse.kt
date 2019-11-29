package com.gnoemes.shikimori.entities

import com.google.gson.annotations.SerializedName

internal data class StatisticResponse(
        @field:SerializedName("name") val name : String,
        @field:SerializedName("value") val value : Int
)