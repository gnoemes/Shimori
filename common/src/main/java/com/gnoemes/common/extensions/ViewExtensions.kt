package com.gnoemes.common.extensions

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.gnoemes.common.utils.Event
import com.google.android.material.snackbar.Snackbar

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


fun View.doOnApplyWindowInsets(
    f: (
        View,
        insets: WindowInsetsCompat,
        initialPadding: ViewDimensions,
        initialMargin: ViewDimensions
    ) -> Unit
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) return

    // Create a snapshot of the view's padding state
    val initialPadding = createStateForViewPadding(this)
    val initialMargin = createStateForViewMargin(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding, initialMargin)
        insets
    }
    requestApplyInsetsWhenAttached()
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
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

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

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
private fun createStateForViewPadding(view: View) =
    ViewDimensions(
        view.paddingLeft,
        view.paddingTop,
        view.paddingRight,
        view.paddingBottom,
        view.paddingStart,
        view.paddingEnd
    )

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
private fun createStateForViewMargin(view: View): ViewDimensions {
    return (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
        ViewDimensions(
            it.leftMargin, it.topMargin, it.rightMargin, it.bottomMargin,
            it.marginStart, it.marginEnd
        )
    } ?: ViewDimensions()
}

data class ViewDimensions(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0,
    val start: Int = 0,
    val end: Int = 0
)