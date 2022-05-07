package com.gnoemes.shimori.common.ui.utils

import android.content.Context

class ShimoriContextTextProvider(
    private val context: Context
) : ShimoriTextProvider {
    override fun text(id: MessageID): String {
        return try {
            val resId = context
                .resources
                .getIdentifier(id.key, "string", context.packageName)
                .also {
                    if (it < 0) throw IllegalArgumentException("#key:${id.key} not found")
                }

            context.getString(resId)
        } catch (e: IllegalArgumentException) {
            e.message.orEmpty()
        }
    }
}