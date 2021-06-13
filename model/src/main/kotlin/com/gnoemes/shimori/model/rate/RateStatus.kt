package com.gnoemes.shimori.model.rate

enum class RateStatus(
    val priority: Int,
    val shikimoriValue: String
) {
    PLANNED(0, "planned"),
    WATCHING(1, "watching"),
    REWATCHING(2, "rewatching"),
    COMPLETED(3, "completed"),
    ON_HOLD(4, "on_hold"),
    DROPPED(5, "dropped");

    companion object {
        fun watchingFirstValues() =
            listOf(WATCHING, PLANNED, REWATCHING, COMPLETED, ON_HOLD, DROPPED)

        fun priorityValues() = values().sortedBy { it.priority }
    }
}