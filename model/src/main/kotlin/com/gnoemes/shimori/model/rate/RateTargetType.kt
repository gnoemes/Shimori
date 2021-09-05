package com.gnoemes.shimori.model.rate

enum class RateTargetType(val type: String) {
    ANIME("anime"),
    MANGA("manga"),
    RANOBE("ranobe"), ;

    val anime get() = this == ANIME
    val manga get() = this == MANGA
    val ranobe get() = this == RANOBE

    companion object {
        fun findOrDefault(otherType: String?): RateTargetType {
            return values().find { it.type == otherType } ?: ANIME
        }
    }
}