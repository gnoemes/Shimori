package com.gnoemes.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gnoemes.common.ui.widgets.dialog.LayoutMode
import com.gnoemes.common.ui.widgets.dialog.ShimoriBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior

abstract class BaseBottomSheetDialog<Binding : ViewDataBinding>(
    private val layoutMode: LayoutMode
) : BaseBindingDialogFragment<Binding>() {
    protected lateinit var dialog: ShimoriBottomSheetDialog

    /**
     * Tracks if we are waiting for a dismissAllowingStateLoss or a regular dismiss once the
     * BottomSheet is hidden and onStateChanged() is called.
     */
    private var waitingForDismissAllowingStateLoss = false

    override fun createDialog(savedInstanceState: Bundle?): Dialog? =
        ShimoriBottomSheetDialog(requireContext(), layoutMode)

    override fun onDialogCreated(dialog: Dialog, savedInstanceState: Bundle?) {
        (dialog as ShimoriBottomSheetDialog).run {
            this@BaseBottomSheetDialog.dialog = dialog
            val dialogView = createDialogView(
                    LayoutInflater.from(requireContext()),
                    dialog.contentLayout,
                    savedInstanceState
            )
            dialog.contentLayout.addCustomView(dialogView)
        }
    }

    override fun dismiss() {
        if (!tryDismissWithAnimation(false)) {
            super.dismiss()
        }
    }

    override fun dismissAllowingStateLoss() {
        if (!tryDismissWithAnimation(true)) {
            super.dismissAllowingStateLoss()
        }
    }

    /**
     * Tries to dismiss the dialog fragment with the bottom sheet animation. Returns true if possible,
     * false otherwise.
     */
    private fun tryDismissWithAnimation(allowingStateLoss: Boolean): Boolean {
        val baseDialog = getDialog()
        if (baseDialog is ShimoriBottomSheetDialog) {
            val behavior = baseDialog.requireBehavior()
            if (behavior.isHideable && baseDialog.dismissWithAnimation) {
                dismissWithAnimation(behavior, allowingStateLoss)
                return true
            }
        }
        return false
    }

    private fun dismissWithAnimation(behavior: BottomSheetBehavior<*>, allowingStateLoss: Boolean) {
        waitingForDismissAllowingStateLoss = allowingStateLoss
        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            dismissAfterAnimation()
        } else {
            behavior.addBottomSheetCallback(BottomSheetDismissCallback())
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private fun dismissAfterAnimation() {
        if (waitingForDismissAllowingStateLoss) {
            super.dismissAllowingStateLoss()
        } else {
            super.dismiss()
        }
    }

    private inner class BottomSheetDismissCallback : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAfterAnimation()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
}

