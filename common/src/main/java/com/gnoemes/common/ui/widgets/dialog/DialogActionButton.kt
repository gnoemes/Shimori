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
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.util.AttributeSet
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton

/**
 * Represents an action button in a dialog, positive, negative, or neutral. Handles switching
 * out its selector, padding, and text alignment based on whether buttons are in stacked mode or not.
 *
 * @author Aidan Follestad (afollestad)
 */
//TODO
class DialogActionButton(
  context: Context,
  attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {

  private var enabledColor: Int = 0
  private var disabledColor: Int = 0
  private var enabledColorOverride: Int? = null

  init {
    isClickable = true
    isFocusable = true
  }

  internal fun update(
    baseContext: Context,
    appContext: Context,
    stacked: Boolean
  ) {
    // Casing
    setSupportAllCaps(true)

    // Text alignment
    if (stacked) setGravityEndCompat()
    else gravity = CENTER

    // Invalidate in case enabled state was changed before this method executed
    isEnabled = isEnabled
  }

  override fun setEnabled(enabled: Boolean) {
    super.setEnabled(enabled)
    setTextColor(if (enabled) enabledColorOverride ?: enabledColor else disabledColor)
  }

    private fun TextView.setGravityEndCompat() {
        if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
        }
        this.gravity = Gravity.END or Gravity.CENTER_VERTICAL
    }
}
