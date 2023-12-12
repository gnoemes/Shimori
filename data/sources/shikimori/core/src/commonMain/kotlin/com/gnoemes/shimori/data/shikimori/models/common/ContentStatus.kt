package com.gnoemes.shimori.data.shikimori.models.common

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
enum class ContentStatus(val status: String) {
    @SerialName("anons")
    ANONS("anons"),
    @SerialName("ongoing")
    ONGOING("ongoing"),
    @SerialName("released")
    RELEASED("released"),
    @SerialName("paused")
    PAUSED("paused"),
    @SerialName("discontinued")
    DISCONTINUED("discontinued"),
}