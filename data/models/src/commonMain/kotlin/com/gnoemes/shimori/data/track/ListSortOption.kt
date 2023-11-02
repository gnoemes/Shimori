package com.gnoemes.shimori.data.track

enum class ListSortOption {
    NAME,
    PROGRESS,

    //Last released
    DATE_AIRED,

    //rating from Track.kt "Your Score"
    MY_SCORE,

    //rating from Entity "User's score"
    RATING,

    //episodes, chapters etc
    SIZE,

    //Last added
    DATE_CREATED,

    //Last changed
    DATE_UPDATED,

    ;

    companion object {
        private val default = listOf(
            PROGRESS,
            SIZE,
            DATE_UPDATED,
            DATE_CREATED,
            DATE_AIRED,
            NAME,
            MY_SCORE,
            RATING,
        )
        val animePriorityValues: List<ListSortOption> = default
        val mangaPriorityValues: List<ListSortOption> = default
        val pinnedPriorityValues: List<ListSortOption> = listOf(
            PROGRESS,
            DATE_UPDATED,
            DATE_CREATED,
            DATE_AIRED,
            NAME,
            MY_SCORE,
            RATING,
        )

        fun priorityForType(type: ListType): List<ListSortOption> = when (type) {
            ListType.Anime -> animePriorityValues
            ListType.Manga -> mangaPriorityValues
            ListType.Ranobe -> default
            ListType.Pinned -> pinnedPriorityValues
            else -> default
        }
    }
}