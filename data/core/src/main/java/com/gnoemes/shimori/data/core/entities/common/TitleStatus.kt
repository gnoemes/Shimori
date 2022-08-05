package com.gnoemes.shimori.data.core.entities.common

@kotlinx.serialization.Serializable
enum class TitleStatus(val status: String) {
    ANONS("anons"),
    ONGOING("ongoing"),
    RELEASED("released"),
    PAUSED("paused"),
}