package com.gnoemes.common.extensions

import android.graphics.Point
import android.view.WindowManager

fun WindowManager.getWidthAndHeight(): Pair<Int, Int> {
    return Point()
        .apply { defaultDisplay.getSize(this) }
        .let { Pair(it.x, it.y) }
}