package com.gnoemes.shimori.rates.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.gnoemes.common.BaseBottomSheetDialog
import com.gnoemes.common.databinding.DialogMenuBinding
import com.gnoemes.common.extensions.navigationBarSize
import com.gnoemes.common.ui.widgets.dialog.LayoutMode
import com.gnoemes.shimori.model.app.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.rates.R
import com.gnoemes.shimori.rates.RateSortTextCreator
import javax.inject.Inject


class RateSortDialogFragment : BaseBottomSheetDialog<DialogMenuBinding>(LayoutMode.WRAP_CONTENT) {
    private val viewModel: RateSortViewModel by fragmentViewModel()

    @Inject
    lateinit var textCreator: RateSortTextCreator

    @Inject
    internal lateinit var viewModelFactory: RateSortViewModel.Factory

    companion object {
        fun newInstance(target: RateTargetType, sort: RateSort) =
            RateSortDialogFragment().apply {
                arguments = bundleOf(
                        "target" to target,
                        "sort" to sort
                )
            }
    }

    override fun createBinding(inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?
    ): DialogMenuBinding {
        return DialogMenuBinding.inflate(inflater, container, false)
    }

    override fun onDialogViewCreated(binding: DialogMenuBinding) {
        with(binding) {
            bottomSheetToolbar.toolbar.setTitle(R.string.rate_sort)

            navView.updatePadding(bottom = requireContext().navigationBarSize() + navView.paddingBottom)

            withState(viewModel) { state ->
                navView.apply {
                    menu.run {
                        state.items.forEachIndexed { index, sort -> add(0, sort.ordinal, index, textCreator.name(state.type, sort)) }
                        setGroupCheckable(0, true, true)
                    }
                    setCheckedItem(state.selected.ordinal)
                    setNavigationItemSelectedListener {
                        val sortOption = RateSortOption.priorityValues()[it.order]
                        viewModel.updateRateSort(sortOption)
                        dismiss()
                        false
                    }
                }
            }
        }
    }

    override fun invalidate(binding: DialogMenuBinding) = withState(viewModel) { state ->
        binding.navView.setCheckedItem(state.selected.ordinal)
    }

}