package com.gnoemes.shimori.data.common

@kotlinx.serialization.Serializable
enum class TitleStatus(val status: String) {
    ANONS("anons"),
    ONGOING("ongoing"),
    RELEASED("released"),
    PAUSED("paused"),
    DISCONTINUED("discontinued");

    companion object {
        fun find(status: String?): TitleStatus? {
            return entries.firstOrNull { it.status == status }
        }
    }
}