package com.gnoemes.shimori.base.utils

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.collection.ArrayMap

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class SharedElementHelper {
    private val _sharedElements = ArrayMap<View, String?>()

    val sharedElements: Map<View, String?>
        get() = _sharedElements

    fun addSharedElement(view: View, transitionName: String? = view.transitionName) {
        _sharedElements[view] = transitionName ?: view.transitionName
    }

    fun isEmpty(): Boolean = _sharedElements.isEmpty
}