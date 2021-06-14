package com.gnoemes.shimori.common.utils

import com.gnoemes.shimori.model.rate.RateStatus

object RateUtils {

//    @DrawableRes
//    fun getIcon(status: RateStatus): Int = when (status) {
//        RateStatus.PLANNED -> R.drawable.ic_rate_planned
//        RateStatus.WATCHING -> R.drawable.ic_rate_watching
//        RateStatus.REWATCHING -> R.drawable.ic_rate_rewatching
//        RateStatus.COMPLETED -> R.drawable.ic_rate_completed
//        RateStatus.ON_HOLD -> R.drawable.ic_rate_on_hold
//        RateStatus.DROPPED -> R.drawable.ic_rate_dropped
//    }
//
//    @StringRes
//    fun getName(type: RateTargetType, status: RateStatus, short: Boolean = false) = when (type) {
//        RateTargetType.ANIME -> getAnimeName(status, short)
//        else -> getMangaName(status, short)
//    }
//
//    @StringRes
//    fun getAnimeName(status: RateStatus, short: Boolean) = when (status) {
//        RateStatus.PLANNED -> if (short) R.string.rate_planned_short else R.string.rate_planned
//        RateStatus.WATCHING -> R.string.rate_watching
//        RateStatus.REWATCHING -> if (short) R.string.rate_rewatch_short else R.string.rate_rewatch
//        RateStatus.COMPLETED -> R.string.rate_watched
//        RateStatus.ON_HOLD -> R.string.rate_on_hold
//        RateStatus.DROPPED -> R.string.rate_dropped
//    }
//
//    @StringRes
//    fun getMangaName(status: RateStatus, short: Boolean) = when (status) {
//        RateStatus.PLANNED -> if (short) R.string.rate_planned_short else R.string.rate_planned
//        RateStatus.WATCHING -> R.string.rate_reading
//        RateStatus.REWATCHING -> R.string.rate_rereading
//        RateStatus.COMPLETED -> R.string.rate_readed
//        RateStatus.ON_HOLD -> R.string.rate_on_hold
//        RateStatus.DROPPED -> R.string.rate_dropped
//    }
//
//    @ColorRes
//    fun getTextColor(status: RateStatus) = when (status) {
//        RateStatus.COMPLETED -> R.color.selector_rate_text_color_completed
//        RateStatus.ON_HOLD -> R.color.selector_rate_text_color_on_hold
//        RateStatus.DROPPED -> R.color.selector_rate_text_color_dropped
//        else -> R.color.selector_rate_text_color_default
//    }
//
//    @ColorRes
//    fun getTransparentColor(status: RateStatus) = when (status) {
//        RateStatus.COMPLETED -> R.color.selector_rate_transparent_completed
//        RateStatus.ON_HOLD -> R.color.selector_rate_transparent_on_hold
//        RateStatus.DROPPED -> R.color.selector_rate_transparent_dropped
//        else -> R.color.selector_rate_transparent_default
//    }
//
//    @ColorRes
//    fun getTextColorSecondary(status: RateStatus) = when (status) {
//        RateStatus.COMPLETED -> R.color.selector_rate_text_color_secondary_completed
//        RateStatus.ON_HOLD -> R.color.selector_rate_text_color_secondary_on_hold
//        RateStatus.DROPPED -> R.color.selector_rate_text_color_secondary_dropped
//        else -> R.color.selector_rate_text_color_secondary_default
//    }
//
//    @DrawableRes
//    fun getMenuBackground(status: RateStatus, large: Boolean = false) = if (large) when (status) {
//        RateStatus.COMPLETED -> R.drawable.selector_rate_menu_completed_large
//        RateStatus.ON_HOLD -> R.drawable.selector_rate_menu_on_hold_large
//        RateStatus.DROPPED -> R.drawable.selector_rate_menu_dropped_large
//        else -> R.drawable.selector_rate_menu_default_large
//    } else when (status) {
//        RateStatus.COMPLETED -> R.drawable.selector_rate_menu_completed
//        RateStatus.ON_HOLD -> R.drawable.selector_rate_menu_on_hold
//        RateStatus.DROPPED -> R.drawable.selector_rate_menu_dropped
//        else -> R.drawable.selector_rate_menu_default
//    }

    fun fromPriority(priority: Int): RateStatus {
        return RateStatus.values().find { it.priority == priority }
            ?: throw IllegalArgumentException("Rate status with $priority priority doesn't exist")
    }
}