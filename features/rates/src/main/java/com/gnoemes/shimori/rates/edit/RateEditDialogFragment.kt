package com.gnoemes.shimori.rates.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.gnoemes.common.BaseBottomSheetDialog
import com.gnoemes.common.extensions.navigationBarSize
import com.gnoemes.common.ui.widgets.dialog.LayoutMode
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.rates.R
import com.gnoemes.shimori.rates.databinding.DialogFragmentEditRateBinding
import javax.inject.Inject
import kotlin.math.roundToInt

class RateEditDialogFragment : BaseBottomSheetDialog<DialogFragmentEditRateBinding>(LayoutMode.WRAP_CONTENT) {
    private val viewModel: RateEditViewModel by fragmentViewModel()

    @Inject
    internal lateinit var viewModelFactory: RateEditViewModel.Factory

    @Inject
    internal lateinit var controller: RateEditStatusController

    override fun createBinding(inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?
    ): DialogFragmentEditRateBinding {
        return DialogFragmentEditRateBinding.inflate(inflater, container, false)
    }

    override fun onDialogViewCreated(binding: DialogFragmentEditRateBinding) {
        with(binding) {
            statusRecyclerView.run {
                setController(controller)
                layoutManager = GridLayoutManager(context, 3)
                setItemSpacingRes(R.dimen.rate_edit_status_vertical_padding)
            }

            editContainer.updatePadding(bottom = requireContext().navigationBarSize() + editContainer.paddingBottom)

            commentView.doOnTextChanged { text, _, _, _ ->
                viewModel.submitAction(RateEditAction.CommentChanged(text?.toString()))
            }

            acceptBtn.setOnClickListener { viewModel.submitAction(RateEditAction.Done); dismiss() }
            deleteBtn.setOnClickListener { viewModel.submitAction(RateEditAction.Delete) }

            with(rateRating) {
                ratingBar.setOnRatingChangeListener { _, rating ->
                    val newRating = (rating * 2).roundToInt()
                    viewModel.submitAction(RateEditAction.RatingChanged(newRating))
                }
                ratingGroup.setOnClickListener {
                    withState(viewModel) {
                        val newRating = when (val currentRating = it.rate?.score) {
                            10, null -> 0
                            else -> currentRating + 1
                        }
                        viewModel.submitAction(RateEditAction.RatingChanged(newRating))
                    }
                }
            }

            with(rateProgress) {
                progressView.doOnTextChanged { text, _, _, _ ->
                    val newProgress = text?.toString()?.toIntOrNull()
                    if (newProgress != null) {
                        viewModel.submitAction(RateEditAction.ProgressChanged(newProgress))
                    }
                }

                rewatchesView.doOnTextChanged { text, _, _, _ ->
                    val newRewatches = text?.toString()?.toIntOrNull()
                    if (newRewatches != null) {
                        viewModel.submitAction(RateEditAction.ReWatchesChanged(newRewatches))
                    }
                }

                progressIncrementView.setOnClickListener {
                    withState(viewModel) { state ->
                        val currentProgress = state.rate?.progress ?: 0
                        val newProgress = currentProgress + 1
                        viewModel.submitAction(RateEditAction.ProgressChanged(newProgress))
                    }
                }

                progressDecrementView.setOnClickListener {
                    withState(viewModel) { state ->
                        val currentProgress = state.rate?.progress ?: 0
                        val newProgress = (currentProgress - 1).let { if (it < 0) 0 else it }
                        viewModel.submitAction(RateEditAction.ProgressChanged(newProgress))
                    }
                }
            }

            controller.callbacks = object : RateEditStatusController.Callbacks {
                override fun onStatusClicked(status: RateStatus) {
                    viewModel.submitAction(RateEditAction.ChangeStatus(status))
                }
            }
        }
    }

    override fun invalidate(binding: DialogFragmentEditRateBinding) =
        withState(viewModel) { state ->
            binding.state = state
            controller.state = state
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controller.cancelPendingModelBuild()
        controller.clear()
    }
}