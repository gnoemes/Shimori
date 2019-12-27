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
        fun values() : List<RateSortOption> = listOf(
                NAME,
                PROGRESS,
                DATE_AIRED,
                DATE_CREATED,
                DATE_UPDATED,
                SCORE,
                SIZE
        )
    }
}