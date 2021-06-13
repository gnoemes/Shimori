package com.gnoemes.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

abstract class BaseBindingFragment<Binding : ViewDataBinding> : BaseFragment() {
    private var binding: Binding? = null

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        createBinding(inflater, container, savedInstanceState)
            .also { binding = it }
            .root

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(requireBinding(), savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        invalidate()
    }

    final override fun invalidate() = invalidate(requireBinding())

    protected fun requireBinding(): Binding = requireNotNull(binding)

    protected abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Binding

    protected abstract fun onViewCreated(binding: Binding, savedInstanceState: Bundle?)

    protected abstract fun invalidate(binding: Binding)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}