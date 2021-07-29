package com.gnoemes.shimori.common.utils

import android.content.Context
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.model.rate.ListsPage
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ShimoriTextCreator @Inject constructor(
    @ActivityContext private val context: Context
) {

    fun listSortText(type: RateTargetType, option: RateSortOption): String {
        val stringRes = when (option) {
            RateSortOption.PROGRESS -> R.string.list_sort_progress
            RateSortOption.DATE_CREATED -> R.string.list_sort_last_added
            RateSortOption.DATE_UPDATED -> R.string.list_sort_last_changed
            RateSortOption.DATE_AIRED -> R.string.list_sort_last_released
            RateSortOption.MY_SCORE -> R.string.list_sort_your_score
            RateSortOption.RATING -> R.string.list_sort_rating
            RateSortOption.NAME -> R.string.list_sort_name
            RateSortOption.SIZE -> {
                when (type) {
                    RateTargetType.ANIME -> R.string.list_sort_size_anime
                    else -> R.string.list_sort_size_manga
                }
            }
        }

        return context.getString(stringRes)
    }

    fun listsPageText(type: RateTargetType, listsPage: ListsPage): String {
        val stringRes = when (listsPage) {
            ListsPage.PINNED -> R.string.List_page_pinned
            ListsPage.WATCHING -> if (type.anime) R.string.List_page_watching else R.string.List_page_reading
            ListsPage.RE_WATCHING -> R.string.List_page_re_watching
            ListsPage.ON_HOLD -> R.string.List_page_on_hold
            ListsPage.PLANNED -> R.string.List_page_planned
            ListsPage.COMPLETED -> R.string.List_page_completed
            ListsPage.DROPPED -> R.string.List_page_dropped
        }

        return context.getString(stringRes)
    }
}