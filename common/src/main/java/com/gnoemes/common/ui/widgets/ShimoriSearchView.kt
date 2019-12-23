package com.gnoemes.common.ui.widgets

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
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

    init {
        findViewById<SearchAutoComplete>(R.id.search_src_text)?.apply {
            updatePadding(context.dp(16), 0, context.dp(8), 0)
            setHintTextColor(context.colorStateList(context.attr(R.attr.colorOnPrimarySecondary).resourceId))
        }

        findViewById<AppCompatImageView>(R.id.search_mag_icon)?.apply {
            updateLayoutParams<LinearLayout.LayoutParams> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = 0
                }
                leftMargin = 0
            }
        }

        findViewById<LinearLayout>(R.id.search_edit_frame)?.apply {
            val margin = context.dp(12)

            updateLayoutParams<LinearLayout.LayoutParams> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    marginStart = margin
                }
                leftMargin = margin
            }
        }
        findViewById<ImageView>(R.id.search_close_btn)?.apply {
            val padding = context.dp(12)
            updatePadding(padding, 0, padding, 0)
            setColorFilter(context.colorAttr(R.attr.colorOnPrimary))
        }
    }
}