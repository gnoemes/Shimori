package com.gnoemes.common.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DimenRes
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.gnoemes.common.utils.Event
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

fun Activity.hideSoftInput() {
    val imm: InputMethodManager? = getSystemService()
    val currentFocus = currentFocus
    if (currentFocus != null && imm != null) {
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun Fragment.hideSoftInput() = requireActivity().hideSoftInput()
fun Fragment.dp(dp: Int) = resources.dp(dp)
fun Activity.dp(dp: Int) = resources.dp(dp)
fun Context.dp(dp: Int) = resources.dp(dp)
fun Resources.dp(dp: Int) = (displayMetrics.density * dp).roundToInt()

fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let {
        Snackbar.make(it.findViewById<View>(android.R.id.content), snackbarText, timeLength).show()
    }
}

fun Fragment.setupSnackbar(lifecycleOwner: LifecycleOwner, snackbarEvent: LiveData<Event<Int>>, timeLength: Int) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let { res ->
            context?.let { showSnackbar(it.getString(res), timeLength) }
        }
    })
}

fun ViewGroup.beginDelayedTransition(duration: Long = 200) {
    TransitionManager.beginDelayedTransition(this, AutoTransition().apply { setDuration(duration) })
}

fun View.dimen(@DimenRes dimenRes : Int) = context.dimen(dimenRes)

inline fun View.doOnSizeChange(crossinline action: (view: View) -> Boolean) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            if ((bottom - top) != (oldBottom - oldTop) || (right - left) != (oldRight - oldLeft)) {
                if (!action(view)) {
                    view.removeOnLayoutChangeListener(this)
                }
            }
        }
    })
}