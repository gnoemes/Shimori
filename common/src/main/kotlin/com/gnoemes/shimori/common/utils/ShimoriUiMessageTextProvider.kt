package com.gnoemes.shimori.common.utils

import android.content.Context
import com.gnoemes.shimori.common.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShimoriUiMessageTextProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun text(id: MessageID): String {
        return when (id) {
            MessageID.DisabledDueEmptyRates -> context.getString(R.string.error_disabled_due_empty_rates)
            else -> "#id:$id replace me#"
        }
    }
}

@JvmInline
value class MessageID private constructor(val id: Int) {
    companion object {
        val DisabledDueEmptyRates = MessageID(0)
    }
}