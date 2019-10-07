package com.gnoemes.common.ui.recyclerview

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shimori.base.extensions.findBaseContext

class HideImeOnScrollListener : RecyclerView.OnScrollListener() {
    private lateinit var imm: InputMethodManager

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING && recyclerView.childCount > 0) {
            if (!::imm.isInitialized) {
                imm = recyclerView.context.getSystemService()!!
            }
            if (imm.isAcceptingText) {
                val activity: Activity? = recyclerView.context.findBaseContext()
                val currentFocus = activity?.currentFocus
                if (currentFocus != null) {
                    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
            }
        }
    }
}