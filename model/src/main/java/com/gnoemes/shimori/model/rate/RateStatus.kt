package com.gnoemes.shimori.model.rate

enum class RateStatus(val shikimoriValue : String) {
    PLANNED("planned"),
    WATCHING("watching"),
    REWATCHING("rewatching"),
    COMPLETED("completed"),
    ON_HOLD("on_hold"),
    DROPPED("dropped");
}