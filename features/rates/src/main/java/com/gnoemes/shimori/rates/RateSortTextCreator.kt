package com.gnoemes.shimori.rates

import android.content.Context
import com.gnoemes.shimori.base.di.PerActivity
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType
import javax.inject.Inject

class RateSortTextCreator @Inject constructor(
    @PerActivity private val context: Context
) {

    fun name(type: RateTargetType, sort: RateSortOption) = when (type) {
        RateTargetType.ANIME -> animeName(sort)
        else -> mangaName(sort)
    }

    fun mangaName(sort: RateSortOption) = when (sort) {
        RateSortOption.NAME -> context.getString(R.string.rate_sort_name)
        RateSortOption.PROGRESS -> context.getString(R.string.rate_sort_progress)
        RateSortOption.DATE_CREATED -> context.getString(R.string.rate_sort_date_added)
        RateSortOption.DATE_UPDATED -> context.getString(R.string.rate_sort_date_updated)
        RateSortOption.DATE_AIRED -> context.getString(R.string.rate_sort_date)
        RateSortOption.SCORE -> context.getString(R.string.rate_sort_score)
        RateSortOption.SIZE -> context.getString(R.string.rate_sort_chapters)
    }

    fun animeName(sort: RateSortOption) = when (sort) {
        RateSortOption.NAME -> context.getString(R.string.rate_sort_name)
        RateSortOption.PROGRESS -> context.getString(R.string.rate_sort_progress)
        RateSortOption.DATE_CREATED -> context.getString(R.string.rate_sort_date_added)
        RateSortOption.DATE_UPDATED -> context.getString(R.string.rate_sort_date_updated)
        RateSortOption.DATE_AIRED -> context.getString(R.string.rate_sort_date)
        RateSortOption.SCORE -> context.getString(R.string.rate_sort_score)
        RateSortOption.SIZE -> context.getString(R.string.rate_sort_episodes)
    }

}