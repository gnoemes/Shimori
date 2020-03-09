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
import android.graphics.Paint
import android.graphics.Paint.Style.STROKE
import android.util.AttributeSet
import android.view.ViewGroup
import com.gnoemes.common.R
import com.gnoemes.common.extensions.colorAttr
import com.gnoemes.common.extensions.dp

abstract class BaseDialogSubLayout internal constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    private val dividerPaint = Paint()
    protected val dividerHeight = context.dp(1)
    lateinit var dialog : ShimoriBottomSheetDialog

    var drawDivider: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    init {
        @Suppress("LeakingThis")
        setWillNotDraw(false)
        dividerPaint.style = STROKE
        dividerPaint.strokeWidth = dividerHeight.toFloat()
        dividerPaint.isAntiAlias = true
    }

    protected fun dividerPaint(): Paint {
        dividerPaint.color = dialog.context.colorAttr(R.attr.colorDivider)
        return dividerPaint
    }
}
