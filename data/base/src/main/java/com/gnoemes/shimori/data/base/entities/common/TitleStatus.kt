package com.gnoemes.shimori.data.base.entities.common

@kotlinx.serialization.Serializable
enum class TitleStatus(val status: String) {
    ANONS("anons"),
    ONGOING("ongoing"),
    RELEASED("released")
}