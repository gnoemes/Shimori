package com.gnoemes.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

abstract class BaseBindingDialogFragment<Binding : ViewDataBinding> : BaseDialogFragment() {
    private var binding: Binding? = null

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    final override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
            createDialog(savedInstanceState) ?: return super.onCreateDialog(savedInstanceState)

        return dialog.apply { onDialogCreated(this, savedInstanceState) }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        onDialogViewCreated(requireBinding())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        invalidate()
    }

    final override fun invalidate() = invalidate(requireBinding())

    protected fun createDialogView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        createBinding(inflater, container, savedInstanceState)
            .also { binding = it }
            .root

    protected fun requireBinding(): Binding = requireNotNull(binding)

    protected abstract fun createDialog(savedInstanceState: Bundle?): Dialog?

    protected abstract fun onDialogCreated(dialog: Dialog, savedInstanceState: Bundle?)

    protected abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Binding

    protected abstract fun onDialogViewCreated(binding: Binding)

    protected abstract fun invalidate(binding: Binding)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}