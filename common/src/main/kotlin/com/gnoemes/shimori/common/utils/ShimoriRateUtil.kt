package com.gnoemes.shimori.common.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ShimoriRateUtil @Inject constructor(
    @ActivityContext private val context: Context,
) {

    fun rateTargetTypeName(type: RateTargetType): String = when (type) {
        RateTargetType.ANIME -> context.getString(R.string.anime)
        RateTargetType.MANGA -> context.getString(R.string.manga)
        RateTargetType.RANOBE -> context.getString(R.string.ranobe)
    }

    @DrawableRes
    fun rateTargetTypeIcon(type: RateTargetType): Int = when (type) {
        RateTargetType.ANIME -> R.drawable.ic_anime
        RateTargetType.MANGA -> R.drawable.ic_manga
        RateTargetType.RANOBE -> R.drawable.ic_ranobe
    }
}