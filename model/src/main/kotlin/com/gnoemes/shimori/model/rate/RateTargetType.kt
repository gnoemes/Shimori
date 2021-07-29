package com.gnoemes.shimori.model.rate

enum class RateTargetType(val type: String) {
    ANIME("anime"),
    MANGA("manga"),
    RANOBE("ranobe"), ;

    val anime get() = this == ANIME

    companion object {
        fun findOrDefault(otherType: String?): RateTargetType {
            return values().find { it.type == otherType } ?: ANIME
        }
    }
}