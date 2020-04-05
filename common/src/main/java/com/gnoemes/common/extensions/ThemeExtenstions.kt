package com.gnoemes.common.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat

fun Resources.Theme.attr(@AttrRes attribute: Int): TypedValue {
    val typedValue = TypedValue()
    require(resolveAttribute(attribute, typedValue, true)) {
        "Failed to resolve attribute: $attribute"
    }

    return typedValue
}

@ColorInt
fun Resources.Theme.color(@AttrRes attribute: Int): Int {
    val attr = attr(attribute)
    require(!(attr.type < TypedValue.TYPE_FIRST_COLOR_INT || attr.type > TypedValue.TYPE_LAST_COLOR_INT)) {
        "Attribute value type is not color: $attribute"
    }

    return attr.data
}

@DrawableRes
fun Resources.Theme.drawable(@AttrRes attribute: Int): Int {
    return attr(attribute).resourceId
}

@ColorInt
fun Context.colorAttr(@AttrRes attribute: Int): Int = theme.color(attribute)

fun Context.drawableAttr(@AttrRes attribute: Int): Drawable? = drawable(theme.drawable(attribute))

fun Context.attr(@AttrRes attribute: Int): TypedValue = theme.attr(attribute)

fun Drawable.tint(@ColorInt color: Int) {
    DrawableCompat.setTint(this, color)
    mutate()
}

fun Drawable.tint(context: Context, @AttrRes attr: Int) {
    val color = context.colorAttr(attr)
    DrawableCompat.setTint(this, color)
    mutate()
}

@Dimension(unit = Dimension.PX)
fun Context.dimenAttr(@AttrRes attribute: Int): Int =
    TypedValue.complexToDimensionPixelSize(attr(attribute).data, resources.displayMetrics)