package com.gnoemes.common.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

@ColorInt
fun Context.color(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.drawable(@DrawableRes drawableResId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableResId)
}

fun Context.colorStateList(@ColorRes colorRes: Int): ColorStateList {
    return AppCompatResources.getColorStateList(this, colorRes)
}

fun Context.dimen(@DimenRes dimen: Int) = this.resources.getDimension(dimen).toInt()

fun Context.navigationBarSize() : Int {
    val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        return resources.getDimensionPixelSize(resourceId)
    }

    return 0
}

fun View.drawable(@DrawableRes drawableResId: Int) : Drawable? = context.drawable(drawableResId)