package com.gnoemes.shimori.base.extensions

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes


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