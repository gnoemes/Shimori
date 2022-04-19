package com.gnoemes.shimori.data.base.entities.rate

enum class RateTargetType {
    ANIME, MANGA, RANOBE, ;

    val anime get() = this == ANIME
    val manga get() = this == MANGA
    val ranobe get() = this == RANOBE

    override fun toString(): String = name.lowercase()
}