package com.gnoemes.shimori.base.shared.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window

fun Context.findWindow(): Window? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context.window
        context = context.baseContext
    }
    return null
}