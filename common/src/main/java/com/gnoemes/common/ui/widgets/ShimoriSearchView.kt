package com.gnoemes.common.ui.widgets

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import com.gnoemes.common.R
import com.gnoemes.common.extensions.attr
import com.gnoemes.common.extensions.colorAttr
import com.gnoemes.common.extensions.colorStateList
import com.gnoemes.common.extensions.dp

class ShimoriSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SearchView(context, attrs, defStyleAttr) {

    private var icon: AppCompatImageView? = null
    private var frame: LinearLayout? = null
    private var input: SearchAutoComplete? = null

    init {
        input = findViewById<SearchAutoComplete>(R.id.search_src_text)
            ?.apply {
                updatePadding(context.dp(16), 0, context.dp(8), 0)
                setHintTextColor(context.colorStateList(context.attr(R.attr.colorOnPrimarySecondary).resourceId))
            }

        icon = findViewById<AppCompatImageView>(R.id.search_mag_icon)?.apply {
            updateLeftMargin(this, 0)
        }

        frame = findViewById<LinearLayout>(R.id.search_edit_frame)?.apply {
            updateLeftMargin(this, context.dp(12))
        }
        findViewById<ImageView>(R.id.search_close_btn)?.apply {
            val padding = context.dp(12)
            updatePadding(padding, 0, padding, 0)
            setColorFilter(context.colorAttr(R.attr.colorOnPrimary))
        }
    }

    fun setIcon(res: Int?) {
        if (res == null || res == 0) {
            icon?.isVisible = false
            input?.updatePaddingRelative(start = 0)
            frame?.let { updateLeftMargin(it, 0) }
            return
        }

        icon?.run {
            isVisible = true
            setImageResource(res)
        }
    }

    private fun updateLeftMargin(view: View, margin: Int) {
        view.updateLayoutParams<LinearLayout.LayoutParams> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                marginStart = margin
            }
            leftMargin = margin
        }
    }
}