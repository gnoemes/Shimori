package com.gnoemes.shimori.base

import android.app.PendingIntent

interface AppNavigator {
    fun provideAuthHandleIntent(requestCode : Int) : PendingIntent
}