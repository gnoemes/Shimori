package com.gnoemes.shimori

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.shikimori.ShikimoriConstants
import com.gnoemes.shimori.base.AppNavigator
import com.gnoemes.shimori.main.MainActivity
import javax.inject.Inject

class ShimoriAppNavigator @Inject constructor(
    private val context: Context
) : AppNavigator {
    override fun provideAuthHandleIntent(requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = ShikimoriConstants.AUTH_HANDLE_ACTION
        }
        return PendingIntent.getActivity(context, requestCode, intent, 0)
    }
}