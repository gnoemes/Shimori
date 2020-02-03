package com.gnoemes.common.databinding

import android.annotation.SuppressLint
import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.gnoemes.common.ui.widgets.MaxLinesToggleClickListener
import com.gnoemes.common.utils.RateUtils
import com.gnoemes.shimori.model.common.ShimoriImage
import com.gnoemes.shimori.model.rate.RateStatus
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily

@BindingAdapter("visible")
fun visible(view: View, value: Boolean) {
    view.isVisible = value
}

@BindingAdapter("visibleIfNotNull")
fun visibleIfNotNull(view: View, any: Any?) {
    view.isVisible = any != null
}

@BindingAdapter("textOrGoneIfEmpty")
fun textOrGoneIfEmpty(view: TextView, s: CharSequence?) {
    view.text = s
    view.isGone = s.isNullOrEmpty()
}

@BindingAdapter("textOrGoneIfNullOrZero")
fun textOrGoneIfNullOrZero(view: TextView, s: Int?) {
    view.text = "$s"
    view.isGone = s == null || s == 0
}

@BindingAdapter("textOrDashIfNullOrZero")
fun textOrDashIfNullOrZero(view: TextView, s: Int?) {
    val exist = s != null && s != 0
    view.text = if (exist) "$s" else "-"
}

@BindingAdapter("srcRes")
fun srcRes(view: ImageView, drawableRes: Int) {
    if (drawableRes == 0) view.setImageDrawable(null)
    else view.setImageResource(drawableRes)
}

@BindingAdapter("maxLinesToggle")
fun maxLinesClickListener(view: TextView, oldCollapsedMaxLines: Int, newCollapsedMaxLines: Int) {
    if (oldCollapsedMaxLines != newCollapsedMaxLines) {
        view.maxLines = newCollapsedMaxLines
        view.setOnClickListener(
                MaxLinesToggleClickListener(
                        newCollapsedMaxLines
                )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@BindingAdapter("roundedCornerOutlineProvider")
fun roundedCornerOutlineProvider(view: View, oldRadius: Float, radius: Float) {
    view.clipToOutline = true
    if (oldRadius != radius) {
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
    }
}

@SuppressLint("UnsafeExperimentalUsageError")
@BindingAdapter("roundedCorners")
fun roundedCorners(view: ShapeableImageView, oldRadius: Float, radius: Float) {
    if (oldRadius != radius) {
        view.shapeAppearanceModel = view.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
    }
}

@BindingAdapter("image")
fun loadImage(view: ImageView, oldImage: ShimoriImage?, image: ShimoriImage?) {
    if (oldImage == image) return

    if (image != null) {
        Glide.with(view)
            .load(image.original)
            .into(view)
    } else {
        view.setImageDrawable(null)
    }
}

@BindingAdapter("rateIcon")
fun rateIcon(button: MaterialButton, status: RateStatus?) {
    if (status == null) return

    button.setIconResource(RateUtils.getIcon(status))
}

@BindingAdapter("query")
fun setQuery(view: androidx.appcompat.widget.SearchView, query: String?) {
    view.setQuery(query, false)
}