package com.gnoemes.common.databinding

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.gnoemes.common.extensions.doOnApplyWindowInsets
import com.gnoemes.common.ui.widgets.MaxLinesToggleClickListener

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

@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    "paddingLeftGestureInsets",
    "paddingTopGestureInsets",
    "paddingRightGestureInsets",
    "paddingBottomGestureInsets",
    "marginLeftSystemWindowInsets",
    "marginTopSystemWindowInsets",
    "marginRightSystemWindowInsets",
    "marginBottomSystemWindowInsets",
    "marginLeftGestureInsets",
    "marginTopGestureInsets",
    "marginRightGestureInsets",
    "marginBottomGestureInsets",
    requireAll = false
)
fun applySystemWindows(
    view: View,
    padSystemWindowLeft: Boolean,
    padSystemWindowTop: Boolean,
    padSystemWindowRight: Boolean,
    padSystemWindowBottom: Boolean,
    padGestureLeft: Boolean,
    padGestureTop: Boolean,
    padGestureRight: Boolean,
    padGestureBottom: Boolean,
    marginSystemWindowLeft: Boolean,
    marginSystemWindowTop: Boolean,
    marginSystemWindowRight: Boolean,
    marginSystemWindowBottom: Boolean,
    marginGestureLeft: Boolean,
    marginGestureTop: Boolean,
    marginGestureRight: Boolean,
    marginGestureBottom: Boolean
) {
    require(((padSystemWindowLeft && padGestureLeft) ||
            (padSystemWindowTop && padGestureTop) ||
            (padSystemWindowRight && padGestureRight) ||
            (padSystemWindowBottom && padGestureBottom) ||
            (marginSystemWindowLeft && marginGestureLeft) ||
            (marginSystemWindowTop && marginGestureTop) ||
            (marginSystemWindowRight && marginGestureRight) ||
            (marginSystemWindowBottom && marginGestureBottom)).not()) {
        "Invalid parameters. Can not request system window and gesture inset handling" +
                " for the same dimension"
    }

    view.doOnApplyWindowInsets { v, insets, initialPadding, initialMargin ->
        // Padding handling
        val paddingLeft = when {
            padGestureLeft -> insets.systemGestureInsets.left
            padSystemWindowLeft -> insets.systemWindowInsetLeft
            else -> 0
        }
        val paddingTop = when {
            padGestureTop -> insets.systemGestureInsets.top
            padSystemWindowTop -> insets.systemWindowInsetTop
            else -> 0
        }
        val paddingRight = when {
            padGestureRight -> insets.systemGestureInsets.right
            padSystemWindowRight -> insets.systemWindowInsetRight
            else -> 0
        }
        val paddingBottom = when {
            padGestureBottom -> insets.systemGestureInsets.bottom
            padSystemWindowBottom -> insets.systemWindowInsetBottom
            else -> 0
        }
        v.setPadding(
            initialPadding.left + paddingLeft,
            initialPadding.top + paddingTop,
            initialPadding.right + paddingRight,
            initialPadding.bottom + paddingBottom
        )

        // Margin handling
        val marginInsetRequested = marginSystemWindowLeft || marginGestureLeft ||
                marginSystemWindowTop || marginGestureTop || marginSystemWindowRight ||
                marginGestureRight || marginSystemWindowBottom || marginGestureBottom
        if (marginInsetRequested) {
            require(v.layoutParams is ViewGroup.MarginLayoutParams) {
                "Margin inset handling requested but view LayoutParams do not" +
                        " extend MarginLayoutParams"
            }

            val marginLeft = when {
                marginGestureLeft -> insets.systemGestureInsets.left
                marginSystemWindowLeft -> insets.systemWindowInsetLeft
                else -> 0
            }
            val marginTop = when {
                marginGestureTop -> insets.systemGestureInsets.top
                marginSystemWindowTop -> insets.systemWindowInsetTop
                else -> 0
            }
            val marginRight = when {
                marginGestureRight -> insets.systemGestureInsets.right
                marginSystemWindowRight -> insets.systemWindowInsetRight
                else -> 0
            }
            val marginBottom = when {
                marginGestureBottom -> insets.systemGestureInsets.bottom
                marginSystemWindowBottom -> insets.systemWindowInsetBottom
                else -> 0
            }
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = initialMargin.left + marginLeft
                topMargin = initialMargin.top + marginTop
                rightMargin = initialMargin.right + marginRight
                bottomMargin = initialMargin.bottom + marginBottom
            }
        }
    }
}