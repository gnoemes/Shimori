package com.gnoemes.common.ui.widgets.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.CheckResult
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import com.gnoemes.common.R
import com.gnoemes.common.extensions.getWidthAndHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import java.lang.Float.isNaN
import kotlin.math.abs
import kotlin.math.min
import kotlin.properties.Delegates.notNull

class ShimoriBottomSheetDialog(
    context: Context,
    private val layoutMode: LayoutMode = LayoutMode.WRAP_CONTENT
) : AppCompatDialog(ContextThemeWrapper(context, R.style.Theme_Shimori), R.style.Theme_Shimori_BottomSheetDialog) {

    private var behavior: BottomSheetBehavior<FrameLayout>? = null
    private lateinit var bottomSheetView: FrameLayout
    private lateinit var rootView: CoordinatorLayout
    lateinit var buttonsLayout: DialogActionButtonLayout
    lateinit var contentLayout: DialogContentLayout

    internal var defaultPeekHeight: Int by notNull()
    internal var maxPeekHeight: Int = -1
    private var actualPeekHeight: Int by notNull()

    var dismissWithAnimation = true

    var dialogCancelable: Boolean = true
    private var dialogCanceledOnTouchOutside = true
    private var dialogCanceledOnTouchOutsideSet = false

    companion object {
        private const val DEFAULT_PEEK_HEIGHT_RATIO = 0.65f
        private const val LAYOUT_PEEK_CHANGE_DURATION_MS = 250L

        private const val BUTTONS_SHOW_START_DELAY_MS = 100L
        private const val BUTTONS_SHOW_DURATION_MS = 180L
    }

    init {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        createView()
        setContentView(rootView)
    }

    private fun createView() {
        val (_, windowHeight) = window!!.windowManager.getWidthAndHeight()
        defaultPeekHeight = (windowHeight * DEFAULT_PEEK_HEIGHT_RATIO).toInt()
        actualPeekHeight = defaultPeekHeight
        maxPeekHeight = windowHeight

        setupBottomSheet()

        (rootView.findViewById(R.id.root) as DialogLayout).also { dialogLayout ->
            dialogLayout.layoutMode = layoutMode
            dialogLayout.attachButtonsLayout(buttonsLayout)
            dialogLayout.attachDialog(this)
        }
    }

    private fun setupBottomSheet() {
        requireBehavior().apply {
            isHideable = true
            peekHeight = 0

            setBottomSheetButtonCallbacks(
                    onSlide = { currentHeight ->
                        val buttonsLayoutHeight = buttonsLayout.measuredHeight

                        if (currentHeight in 1..buttonsLayoutHeight) {
                            val diff = buttonsLayoutHeight - currentHeight
                            buttonsLayout.translationY = diff.toFloat()
                        } else if (currentHeight > 0) {
                            buttonsLayout.translationY = 0f
                        }

                        invalidateDividers(currentHeight)
                    },
                    onHide = {
                        buttonsLayout.isVisible = false
                        dismiss()
                    }
            )

            bottomSheetView.waitForHeight {
                actualPeekHeight =
                    min(defaultPeekHeight, min(this.measuredHeight, defaultPeekHeight))
            }
        }
    }

    private fun invalidateDividers(currentHeight: Int) {
        //TODO
        buttonsLayout.drawDivider = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }


    override fun setCancelable(flag: Boolean) {
        super.setCancelable(flag)
        if (dialogCancelable != flag) {
            dialogCancelable = flag
            behavior?.let { it.isHideable = flag }
        }
    }

    override fun onStart() {
        super.onStart()
        if (behavior != null && behavior!!.state == STATE_HIDDEN) {
            behavior!!.state = STATE_COLLAPSED
        }
    }

    private fun preShow() {
        if (dialogCanceledOnTouchOutside && dialogCancelable) {
            rootView.setOnClickListener { dismiss() }
            behavior?.isHideable = true
        } else {
            rootView.setOnClickListener(null)
            behavior?.isHideable = false
        }

        bottomSheetView.waitForHeight {
            behavior?.apply {
                peekHeight = 0
                state = STATE_COLLAPSED
                animatePeekHeight(
                        view = bottomSheetView,
                        start = 0,
                        dest = actualPeekHeight,
                        duration = LAYOUT_PEEK_CHANGE_DURATION_MS,
                        onEnd = {
                            invalidateDividers(actualPeekHeight)
                        }
                )
            }

            showButtons()
        }
    }

    override fun show() {
        preShow()
        super.show()
    }

    private fun onDismiss(): Boolean {
        val sheetBehavior = behavior
        if (sheetBehavior != null &&
            sheetBehavior.state != STATE_HIDDEN
        ) {
            sheetBehavior.apply {
                isHideable = true
                state = STATE_HIDDEN
            }
            hideButtons()
            return true
        }
        return false
    }

    fun onActionButtonClicked(button: WhichButton) {
        //TODO
    }

    private fun showButtons() {
        if (!buttonsLayout.shouldBeVisible()) {
            return
        }
        val start = buttonsLayout.measuredHeight
        buttonsLayout.apply {
            translationY = start.toFloat()
            visibility = View.VISIBLE
        }
        val animator = animateValues(
                from = start,
                to = 0,
                duration = BUTTONS_SHOW_DURATION_MS,
                onUpdate = { buttonsLayout.translationY = it.toFloat() }
        )
        buttonsLayout.onDetach { animator.cancel() }
        animator.apply {
            startDelay = BUTTONS_SHOW_START_DELAY_MS
            start()
        }
    }

    private fun hideButtons() {
        if (!buttonsLayout.shouldBeVisible()) return
        val animator = animateValues(
                from = 0,
                to = buttonsLayout.measuredHeight,
                duration = LAYOUT_PEEK_CHANGE_DURATION_MS,
                onUpdate = { buttonsLayout.translationY = it.toFloat() }
        )
        buttonsLayout.onDetach { animator.cancel() }
        animator.start()
    }

    override fun cancel() {
        val behavior: BottomSheetBehavior<FrameLayout> = requireBehavior()

        if (!dismissWithAnimation || behavior.state == STATE_HIDDEN) {
            super.cancel()
        } else {
            behavior.setState(STATE_HIDDEN)
        }
    }

    override fun dismiss() {
        if (onDismiss()) return
        if (isShowing) super.dismiss()
    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        if (cancel && !dialogCancelable) {
            dialogCancelable = true
        }
        dialogCanceledOnTouchOutside = cancel
        dialogCanceledOnTouchOutsideSet = true
    }

    fun requireBehavior(): BottomSheetBehavior<FrameLayout> {
        if (behavior == null) {
            ensureContainerAndBehavior()
        }
        return behavior!!
    }

    private fun ensureContainerAndBehavior(): CoordinatorLayout {
        rootView =
            View.inflate(context, R.layout.dialog_base_bottom_sheet, null) as CoordinatorLayout
        bottomSheetView = rootView.findViewById(R.id.rootBottomSheet)
        buttonsLayout = rootView.findViewById(R.id.buttonLayout)
        contentLayout = rootView.findViewById(R.id.contentLayout)
        behavior = BottomSheetBehavior.from(bottomSheetView).apply {
            isHideable = dialogCancelable
        }
        return rootView
    }

    private fun BottomSheetBehavior<*>.setBottomSheetButtonCallbacks(
        onSlide: (currentHeight: Int) -> Unit,
        onHide: () -> Unit
    ) {
        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            private var currentState: Int = STATE_COLLAPSED

            override fun onSlide(bottomSheet: View, dY: Float) {
                if (state == STATE_HIDDEN) return

                val percentage = if (isNaN(dY)) 0f else dY
                if (percentage > 0f) {
                    val diff = peekHeight * abs(percentage)
                    onSlide((peekHeight + diff).toInt())
                } else {
                    val diff = peekHeight * abs(percentage)
                    onSlide((peekHeight - diff).toInt())
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                currentState = state
                if (state == STATE_HIDDEN) onHide()
            }
        })
    }

    private fun <T : View> T.waitForHeight(block: T.() -> Unit) {
        if (measuredWidth > 0 && measuredHeight > 0) {
            this.block()
            return
        }

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            var lastHeight: Int? = null

            override fun onGlobalLayout() {
                if (lastHeight != null && lastHeight == measuredHeight) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    return
                }
                if (measuredWidth > 0 && measuredHeight > 0 && lastHeight != measuredHeight) {
                    lastHeight = measuredHeight
                    this@waitForHeight.block()
                }
            }
        })
    }

    private fun BottomSheetBehavior<*>.animatePeekHeight(
        view: View,
        start: Int = peekHeight,
        dest: Int,
        duration: Long,
        onEnd: () -> Unit = {}
    ) {
        if (dest == start) {
            return
        } else if (duration <= 0) {
            peekHeight = dest
            return
        }
        val animator = animateValues(
                from = start,
                to = dest,
                duration = duration,
                onUpdate = this::setPeekHeight,
                onEnd = onEnd
        )
        view.onDetach { animator.cancel() }
        animator.start()
    }

    @CheckResult
    private fun animateValues(
        from: Int,
        to: Int,
        duration: Long,
        onUpdate: (currentValue: Int) -> Unit,
        onEnd: () -> Unit = {}
    ): Animator {
        return ValueAnimator.ofInt(from, to)
            .apply {
                this.interpolator = DecelerateInterpolator()
                this.duration = duration
                addUpdateListener {
                    onUpdate(it.animatedValue as Int)
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) = onEnd()
                })
            }
    }

    private fun <T : View> T.onDetach(onAttached: T.() -> Unit) {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            @Suppress("UNCHECKED_CAST")
            override fun onViewDetachedFromWindow(v: View) {
                removeOnAttachStateChangeListener(this)
                (v as T).onAttached()
            }

            override fun onViewAttachedToWindow(v: View) = Unit
        })
    }

}

