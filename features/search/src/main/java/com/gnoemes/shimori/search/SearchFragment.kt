package com.gnoemes.shimori.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.gnoemes.common.BaseBindingFragment
import com.gnoemes.common.extensions.dimen
import com.gnoemes.common.extensions.dp
import com.gnoemes.shimori.search.databinding.FragmentSearchBinding
import javax.inject.Inject

class SearchFragment : BaseBindingFragment<FragmentSearchBinding>() {
    private val viewModel: SearchViewModel by fragmentViewModel()

    @Inject
    lateinit var viewModelFactory: SearchViewModel.Factory
    @Inject
    lateinit var controller: SearchEpoxyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller.onRestoreInstanceState(savedInstanceState)
    }

    override fun createBinding(inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentSearchBinding, savedInstanceState: Bundle?) {
        binding.recyclerView.run {
            setController(controller)

            //4:3
            val posterWidthWithPadding =
                context.dimen(R.dimen.search_image_grid_poster_height) * 0.75 + dp(16)
            val rawColumns = context.resources.displayMetrics
                .widthPixels
                .div(posterWidthWithPadding)
                .toInt()

            layoutManager = GridLayoutManager(context, rawColumns)
            setItemSpacingDp(16)
        }
    }

    override fun invalidate(binding: FragmentSearchBinding) = withState(viewModel) { state ->
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