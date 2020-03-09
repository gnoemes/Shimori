package com.gnoemes.shimori.model.rate

enum class RateSortOption {
    NAME,
    PROGRESS,
    DATE_AIRED,
    SCORE,
    SIZE,
    DATE_CREATED,
    DATE_UPDATED;

    companion object {
        fun priorityValues() : List<RateSortOption> = listOf(
                NAME,
                PROGRESS,
                DATE_CREATED,
                DATE_UPDATED,
                DATE_AIRED,
                SCORE,
                SIZE
        )
    }
}