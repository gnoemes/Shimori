package com.gnoemes.shimori.common.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ShimoriRateUtil @Inject constructor(
    @ActivityContext private val context: Context,
) {

    fun listTypeName(type: ListType): String = when (type) {
        ListType.Pinned -> context.getString(R.string.pinned)
        ListType.Anime -> context.getString(R.string.anime)
        ListType.Manga -> context.getString(R.string.manga)
        else -> context.getString(R.string.ranobe)
    }

    @DrawableRes
    fun listTypeIcon(type: ListType): Int = when (type) {
        ListType.Pinned -> R.drawable.ic_pin_big
        ListType.Anime -> R.drawable.ic_anime
        ListType.Manga -> R.drawable.ic_manga
        else -> R.drawable.ic_ranobe
    }

    @DrawableRes
    fun rateStatusIcon(status: RateStatus): Int = when(status) {
        RateStatus.PLANNED -> R.drawable.ic_planned
        RateStatus.WATCHING -> R.drawable.ic_in_progress
        RateStatus.REWATCHING -> R.drawable.ic_re_do
        RateStatus.COMPLETED -> R.drawable.ic_completed
        RateStatus.ON_HOLD -> R.drawable.ic_on_hold
        RateStatus.DROPPED -> R.drawable.ic_dropped
    }
}