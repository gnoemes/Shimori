package com.gnoemes.common.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

/**
 * Call [View.requestApplyInsets] in a safe away. If we're attached it calls it straight-away.
 * If not it sets an [View.OnAttachStateChangeListener] and waits to be attached before calling
 * [View.requestApplyInsets].
 */
fun View.requestApplyInsetsWhenAttached() = doOnAttach {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
        it.requestApplyInsets()
    }
}

fun View.doOnAttach(f: (View) -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return

    if (isAttachedToWindow) {
        f(this)
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                f(v)
                removeOnAttachStateChangeListener(this)
            }

            override fun onViewDetachedFromWindow(v: View) {
                removeOnAttachStateChangeListener(this)
            }
        })
    }
}

/**
 * Allows easy listening to layout passing. Return [true] if you need the listener to keep being
 * attached.
 */
inline fun View.doOnLayouts(crossinline action: (view: View) -> Boolean) {
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
            if (!action(view)) {
                view.removeOnLayoutChangeListener(this)
            }
        }
    })
}