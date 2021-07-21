package com.gnoemes.shimori.model.rate

enum class RateSortOption {
    NAME,
    PROGRESS,
    //Last released
    DATE_AIRED,
    //rating from Rate.kt "Your Score"
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

        val animePriorityValues : List<RateSortOption> = default

        fun priorityForType(type: RateTargetType): List<RateSortOption> = when(type) {
            RateTargetType.ANIME -> animePriorityValues
            RateTargetType.MANGA -> default
            RateTargetType.RANOBE -> default
        }
    }
}