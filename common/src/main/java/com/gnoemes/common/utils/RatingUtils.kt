package com.gnoemes.common.utils

import com.gnoemes.common.R

object RatingUtils {

    fun getDescription(rating : Int?) = when(rating) {
        1 -> R.string.rating_bad_ass
        2 -> R.string.rating_awful
        3 -> R.string.rating_very_bad
        4 -> R.string.rating_bad
        5 -> R.string.rating_not_bad
        6 -> R.string.rating_normal
        7 -> R.string.rating_good
        8 -> R.string.rating_fine
        9 -> R.string.rating_nuts
        10 -> R.string.rating_perfect
        else -> R.string.rating_empty
    }
}