package com.gnoemes.shimori.common.ui.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.ui.R

class ShimoriTrackUtil constructor(
    private val context: Context,
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
    fun trackStatusIcon(status: TrackStatus): Int = when(status) {
        TrackStatus.PLANNED -> R.drawable.ic_planned
        TrackStatus.WATCHING -> R.drawable.ic_in_progress
        TrackStatus.REWATCHING -> R.drawable.ic_re_do
        TrackStatus.COMPLETED -> R.drawable.ic_completed
        TrackStatus.ON_HOLD -> R.drawable.ic_on_hold
        TrackStatus.DROPPED -> R.drawable.ic_dropped
    }
}