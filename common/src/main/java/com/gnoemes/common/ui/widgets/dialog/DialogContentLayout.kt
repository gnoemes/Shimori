/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gnoemes.common.ui.widgets.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.*
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gnoemes.common.extensions.dp

/**
 * The middle section of the dialog, between [DialogTitleLayout] and
 * [DialogActionButtonLayout], which contains content such as messages,
 * lists, etc.
 *
 * @author Aidan Follestad (afollestad)
 */
//TODO
class DialogContentLayout(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val rootLayout: DialogLayout?
        get() = parent as DialogLayout
    private var useHorizontalPadding: Boolean = false
    private val frameHorizontalMargin: Int by lazy {
        context.dp(24)
    }

    var customView: View? = null

    fun addCustomView(
        view: View?
    ): View {
        check(customView == null) { "Custom view already set." }

        if (view != null && view.parent != null) {
            // Make sure the view is detached from any former parents.
            val parent = view.parent as? ViewGroup
            parent?.let { parent.removeView(view) }
        }

        customView = view
        addView(customView)

        return customView!!
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        val specWidth = getSize(widthMeasureSpec)
        val specHeight = getSize(heightMeasureSpec)

        val scrollViewHeight = 0
        val remainingHeightAfterScrollView = specHeight - scrollViewHeight
        val childCountWithoutScrollView = childCount

        if (childCountWithoutScrollView == 0) {
            // No more children to measure
            setMeasuredDimension(specWidth, scrollViewHeight)
            return
        }

        val heightPerRemainingChild = remainingHeightAfterScrollView / childCountWithoutScrollView

        var totalChildHeight = scrollViewHeight
        for (i in 0 until childCount) {
            val currentChild = getChildAt(i)
            currentChild.measure(
                    if (currentChild == customView && useHorizontalPadding) {
                        makeMeasureSpec(specWidth - (frameHorizontalMargin * 2), EXACTLY)
                    } else {
                        makeMeasureSpec(specWidth, EXACTLY)
                    },
                    makeMeasureSpec(heightPerRemainingChild, AT_MOST)
            )
            totalChildHeight += currentChild.measuredHeight
        }

        setMeasuredDimension(specWidth, totalChildHeight)
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        var currentTop = 0
        for (i in 0 until childCount) {
            val currentChild = getChildAt(i)
            val currentBottom = currentTop + currentChild.measuredHeight
            val childLeft: Int
            val childRight: Int
            if (currentChild == customView && useHorizontalPadding) {
                childLeft = frameHorizontalMargin
                childRight = measuredWidth - frameHorizontalMargin
            } else {
                childLeft = 0
                childRight = measuredWidth
            }
            currentChild.layout(
                    /*left=   */childLeft,
                    /*top=    */currentTop,
                    /*right=  */childRight,
                    /*bottom= */currentBottom
            )
            currentTop = currentBottom
        }
    }
}
