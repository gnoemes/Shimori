package com.gnoemes.shikimori.entities.common

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
enum class ContentStatus(val status: String) {
    @SerialName("anons")
    ANONS("anons"),
    @SerialName("ongoing")
    ONGOING("ongoing"),
    @SerialName("released")
    RELEASED("released")
}