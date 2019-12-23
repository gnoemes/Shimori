package com.gnoemes.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.gnoemes.common.ui.widgets.OffsetEpoxyRecyclerView
import kotlin.math.max
import kotlin.math.min

class HideTopViewOnScrollBehavior<V : View> @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<V>(context, attrs) {

    companion object {
        private const val STATE_SCROLLED_DOWN = 1
        private const val STATE_SCROLLED_UP = 2
    }

    private var height = 0
    private var currentState = STATE_SCROLLED_DOWN
    private var additionalHiddenOffsetY = 0

    private var recycler: OffsetEpoxyRecyclerView? = null

    override fun onLayoutChild(parent: CoordinatorLayout,
                               child: V,
                               layoutDirection: Int): Boolean {
        val paramsCompat = (child.layoutParams as ViewGroup.MarginLayoutParams)
        height = child.measuredHeight + paramsCompat.topMargin
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    fun setAdditionalHiddenOffsetY(child: V, @Dimension offset: Int) {
        additionalHiddenOffsetY = offset

        if (currentState == STATE_SCROLLED_DOWN) {
            child.translationY = (-height - additionalHiddenOffsetY).toFloat()
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        if (target is OffsetEpoxyRecyclerView) {
            recycler = target
        } else if (target is ViewGroup) {
            recycler = target.findViewWithTag(OffsetEpoxyRecyclerView.TAG)
        }

        recycler?.run {
            offsetLimitY = this@HideTopViewOnScrollBehavior.height + additionalHiddenOffsetY
        }

        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        val offset = if (currentState == STATE_SCROLLED_UP) {
            min(recycler!!.offsetY, height + additionalHiddenOffsetY)
        } else {
            max(0, recycler!!.offsetY)
        }

        if (dyConsumed < 0) {
            currentState = STATE_SCROLLED_DOWN
            recycler?.let {
                child.translationY = -offset.toFloat()
            }
        } else if (dyConsumed > 0) {
            currentState = STATE_SCROLLED_UP
            recycler?.let {
                child.translationY = -offset.toFloat()
            }
        }
    }

    override fun onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams()
        recycler = null
    }
}