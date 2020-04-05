package com.gnoemes.shimori.rates.edit

import androidx.databinding.BindingAdapter
import com.gnoemes.common.extensions.drawable
import com.gnoemes.common.extensions.tint
import com.gnoemes.shimori.rates.R
import com.willy.ratingbar.ScaleRatingBar

@BindingAdapter("emptyDrawable")
fun emptyDrawable(view: ScaleRatingBar, res: Int) {
    val drawable = view.drawable(res)?.apply { tint(view.context, R.attr.colorOnPrimarySecondary) }
    view.setEmptyDrawable(drawable)
}

@BindingAdapter("filledDrawable")
fun filledDrawable(view: ScaleRatingBar, res: Int) {
    val drawable = view.drawable(res)?.apply { tint(view.context, R.attr.colorSecondary) }
    view.setEmptyDrawable(drawable)
}

@BindingAdapter("rating")
fun rating(view: ScaleRatingBar, rating: Int?) {
    val divider = 10f / view.numStars
    view.rating = rating?.div(divider) ?: 0f
}