package com.gnoemes.common.ui.widgets

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager

class MaxLinesToggleClickListener(private val collapsedLines: Int) : View.OnClickListener {
    private val transition = ChangeBounds().apply {
        duration = 200
        interpolator = FastOutSlowInInterpolator()
    }

    override fun onClick(view: View) {
        TransitionManager.beginDelayedTransition(findParent(view), transition)
        val textView = view as TextView
        textView.maxLines = if (textView.maxLines > collapsedLines) collapsedLines else Int.MAX_VALUE
    }

    private fun findParent(view: View): ViewGroup {
        var parentView: View? = view
        while (parentView != null) {
            val parent = parentView.parent as View?
            if (parent is RecyclerView) {
                return parent
            }
            parentView = parent
        }
        return view.parent as ViewGroup
    }
}