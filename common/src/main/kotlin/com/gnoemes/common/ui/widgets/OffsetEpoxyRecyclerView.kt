package com.gnoemes.common.ui.widgets

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.EpoxyRecyclerView
import kotlin.math.max
import kotlin.math.min

class OffsetEpoxyRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : EpoxyRecyclerView(context, attrs, defStyleAttr) {
    private var _offsetX = 0
    private var _offsetY = 0

    var offsetLimitY = -1
    var offsetLimitX = -1

    companion object {
        const val TAG = "offsetRecycler"
    }

    init {
        tag = TAG
    }

    val offsetX: Int
        get() = _offsetX

    val offsetY: Int
        get() = _offsetY

    override fun onScrolled(dx: Int, dy: Int) {
        if (offsetLimitX == -1) _offsetX += dx
        else _offsetX = min(max(_offsetX + dx, 0), offsetLimitX)

        if (offsetLimitY == -1) _offsetY += dy
        else _offsetY = min(max(_offsetY + dy, 0), offsetLimitY)
    }
}