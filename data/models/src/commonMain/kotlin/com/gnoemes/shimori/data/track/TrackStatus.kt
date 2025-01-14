package com.gnoemes.shimori.data.track

enum class TrackStatus(
    val priority: Int
) {
    PLANNED(0),
    WATCHING(1),
    REWATCHING(2),
    COMPLETED(3),
    ON_HOLD(4),
    DROPPED(5);

    companion object {
        fun watchingFirstValues() =
            listOf(WATCHING, PLANNED, REWATCHING, COMPLETED, ON_HOLD, DROPPED)

        val listPagesOrder = listOf(WATCHING, REWATCHING, ON_HOLD, PLANNED, COMPLETED, DROPPED)

        fun priorityValues() = entries.sortedBy { it.priority }
    }
}