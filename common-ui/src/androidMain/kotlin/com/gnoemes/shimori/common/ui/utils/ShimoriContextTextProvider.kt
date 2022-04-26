package com.gnoemes.shimori.common.ui.utils

import android.content.Context
import androidx.annotation.StringRes
import com.gnoemes.shimori.ui.R

class ShimoriContextTextProvider(
    private val context: Context
) : ShimoriTextProvider {
    override fun text(id: MessageID): String = when (id) {
        MessageID.Today -> str(R.string.today)
        else -> "#id:$id replace me#"
    }

    private fun str(@StringRes id: Int) = context.getString(id)
}