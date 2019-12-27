package com.gnoemes.common.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gnoemes.common.R
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType

object RateUtils {

    @DrawableRes
    fun getIcon(status: RateStatus): Int = when (status) {
        RateStatus.PLANNED -> R.drawable.ic_rate_planned
        RateStatus.WATCHING -> R.drawable.ic_rate_watching
        RateStatus.REWATCHING -> R.drawable.ic_rate_rewatching
        RateStatus.COMPLETED -> R.drawable.ic_rate_completed
        RateStatus.ON_HOLD -> R.drawable.ic_rate_on_hold
        RateStatus.DROPPED -> R.drawable.ic_rate_dropped
    }

    @StringRes
    fun getName(type: RateTargetType, status: RateStatus) = when (type) {
        RateTargetType.ANIME -> getAnimeName(status)
        else -> getMangaName(status)
    }

    @StringRes
    fun getAnimeName(status: RateStatus) = when (status) {
        RateStatus.PLANNED -> R.string.rate_planned
        RateStatus.WATCHING -> R.string.rate_watching
        RateStatus.REWATCHING -> R.string.rate_rewatch
        RateStatus.COMPLETED -> R.string.rate_watched
        RateStatus.ON_HOLD -> R.string.rate_on_hold
        RateStatus.DROPPED -> R.string.rate_dropped
    }

    @StringRes
    fun getMangaName(status: RateStatus) = when (status) {
        RateStatus.PLANNED -> R.string.rate_planned
        RateStatus.WATCHING -> R.string.rate_reading
        RateStatus.REWATCHING -> R.string.rate_rereading
        RateStatus.COMPLETED -> R.string.rate_readed
        RateStatus.ON_HOLD -> R.string.rate_on_hold
        RateStatus.DROPPED -> R.string.rate_dropped
    }
}