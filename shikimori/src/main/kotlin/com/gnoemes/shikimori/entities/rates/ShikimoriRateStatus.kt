package com.gnoemes.shikimori.entities.rates

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal enum class ShikimoriRateStatus(val status: String) {
    @SerialName("planned")
    PLANNED("planned"),

    @SerialName("watching")
    WATCHING("watching"),

    @SerialName("rewatching")
    REWATCHING("rewatching"),

    @SerialName("completed")
    COMPLETED("completed"),

    @SerialName("on_hold")
    ON_HOLD("on_hold"),

    @SerialName("dropped")
    DROPPED("dropped");

    override fun toString(): String = status
}