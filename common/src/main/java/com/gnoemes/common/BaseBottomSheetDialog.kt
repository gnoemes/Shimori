package com.gnoemes.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import com.gnoemes.common.ui.widgets.dialog.LayoutMode
import com.gnoemes.common.ui.widgets.dialog.ShimoriBottomSheetDialog

abstract class BaseBottomSheetDialog<Binding : ViewDataBinding>(
    private val layoutMode: LayoutMode
) : BaseBindingDialogFragment<Binding>() {
    protected lateinit var dialog: ShimoriBottomSheetDialog

    override fun createDialog(savedInstanceState: Bundle?): Dialog?
            = ShimoriBottomSheetDialog(requireContext(), layoutMode)

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

}

