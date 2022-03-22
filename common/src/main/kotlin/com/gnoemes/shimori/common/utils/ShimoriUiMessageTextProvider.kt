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
            MessageID.DisabledDueEmptyRates -> context.getString(R.string.notification_error_disabled_due_empty_rates)
            MessageID.TitlePinned -> context.getString(R.string.notification_title_pinned)
            MessageID.TitleUnPinned -> context.getString(R.string.notification_title_unpinned)
            MessageID.Undo -> context.getString(R.string.action_undo)
            else -> "#id:$id replace me#"
        }
    }
}

@JvmInline
value class MessageID private constructor(val id: Int) {
    companion object {
        val DisabledDueEmptyRates = MessageID(0)
        val TitlePinned = MessageID(1)
        val TitleUnPinned = MessageID(2)
        val Undo = MessageID(3)
    }
}