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
import android.content.Context.WINDOW_SERVICE
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Path.Direction.CW
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import android.view.WindowManager
import android.widget.FrameLayout
import com.gnoemes.common.R
import com.gnoemes.common.extensions.getWidthAndHeight

/**
 * The root layout of a dialog. Contains a [DialogTitleLayout], [DialogContentLayout],
 * and [DialogActionButtonLayout].
 *
 * @author Aidan Follestad (afollestad)
 */
//TODO
class DialogLayout(
  context: Context,
  attrs: AttributeSet?
) : FrameLayout(context, attrs) {

  var maxHeight: Int = 0
  var cornerRadii: FloatArray = floatArrayOf()
    set(value) {
      field = value
      if (!cornerRadiusPath.isEmpty) {
        cornerRadiusPath.reset()
      }
      invalidate()
    }

  lateinit var dialog: ShimoriBottomSheetDialog
  lateinit var contentLayout: DialogContentLayout
  var buttonsLayout: DialogActionButtonLayout? = null
  var layoutMode: LayoutMode = LayoutMode.WRAP_CONTENT

  private var isButtonsLayoutAChild: Boolean = true
  private var windowHeight: Int = -1
  private val cornerRadiusPath = Path()
  private val cornerRadiusRect = RectF()

  override fun onFinishInflate() {
    super.onFinishInflate()
    contentLayout = findViewById(R.id.contentLayout)
    buttonsLayout = findViewById(R.id.buttonLayout)
  }

  fun attachDialog(dialog: ShimoriBottomSheetDialog) {
    buttonsLayout?.dialog = dialog
  }

  fun attachButtonsLayout(buttonsLayout: DialogActionButtonLayout) {
    this.buttonsLayout = buttonsLayout
    this.isButtonsLayoutAChild = false
  }

  /**
   * Shows or hides the top and bottom dividers, which separate the title, content, and buttons.
   */
  fun invalidateDividers(
    showBottom: Boolean
  ) {
    buttonsLayout?.drawDivider = showBottom
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    val (_, windowHeight) = windowManager.getWidthAndHeight()
    this.windowHeight = windowHeight
  }

  override fun onMeasure(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int
  ) {
    val specWidth = getSize(widthMeasureSpec)
    var specHeight = getSize(heightMeasureSpec)
    if (maxHeight in 1 until specHeight) {
      specHeight = maxHeight
    }

    if (buttonsLayout.shouldBeVisible()) {
      buttonsLayout!!.measure(
          makeMeasureSpec(specWidth, EXACTLY),
          makeMeasureSpec(0, UNSPECIFIED)
      )
    }

    val buttonsHeight = buttonsLayout?.measuredHeight ?: 0
    val remainingHeight = specHeight - buttonsHeight
    contentLayout.measure(
        makeMeasureSpec(specWidth, EXACTLY),
        makeMeasureSpec(remainingHeight, AT_MOST)
    )

    if (layoutMode == LayoutMode.WRAP_CONTENT) {
      val totalHeight =
          contentLayout.measuredHeight +
          (buttonsLayout?.measuredHeight ?: 0)
      setMeasuredDimension(specWidth, totalHeight)
    } else {
      setMeasuredDimension(specWidth, windowHeight)
    }

    if (cornerRadii.isNotEmpty()) {
      cornerRadiusRect.apply {
        left = 0f
        top = 0f
        right = measuredWidth.toFloat()
        bottom = measuredHeight.toFloat()
      }
      cornerRadiusPath.addRoundRect(cornerRadiusRect, cornerRadii, CW)
    }
  }

  override fun onLayout(
    changed: Boolean,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
  ) {
    val buttonsTop: Int
    if (isButtonsLayoutAChild) {
      buttonsTop =
        measuredHeight - (buttonsLayout?.measuredHeight ?: 0)
      if (buttonsLayout.shouldBeVisible()) {
        val buttonsLeft = 0
        val buttonsRight = measuredWidth
        val buttonsBottom = measuredHeight
        buttonsLayout!!.layout(
            buttonsLeft,
            buttonsTop,
            buttonsRight,
            buttonsBottom
        )
      }
    } else {
      buttonsTop = measuredHeight
    }

    val contentLeft = 0
    val contentRight = measuredWidth
    contentLayout.layout(
        contentLeft,
        0,
        contentRight,
        buttonsTop
    )
  }

  override fun dispatchDraw(canvas: Canvas) {
    if (cornerRadii.isNotEmpty()) {
      canvas.clipPath(cornerRadiusPath)
    }
    super.dispatchDraw(canvas)
  }
}
