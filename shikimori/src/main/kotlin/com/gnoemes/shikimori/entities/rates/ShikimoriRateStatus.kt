package com.gnoemes.shikimori.entities.rates

import com.google.gson.annotations.SerializedName

internal enum class ShikimoriRateStatus(val status : String) {
    @SerializedName("planned")
    PLANNED("planned"),
    @SerializedName("watching")
    WATCHING("watching"),
    @SerializedName("rewatching")
    REWATCHING("rewatching"),
    @SerializedName("completed")
    COMPLETED("completed"),
    @SerializedName("on_hold")
    ON_HOLD("on_hold"),
    @SerializedName("dropped")
    DROPPED("dropped");
}