package com.gnoemes.shimori.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.updatePadding
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.gnoemes.common.BaseBindingFragment
import com.gnoemes.common.extensions.doOnSizeChange
import com.gnoemes.common.extensions.dp
import com.gnoemes.common.extensions.hideSoftInput
import com.gnoemes.common.ui.recyclerview.HideImeOnScrollListener
import com.gnoemes.shimori.calendar.databinding.FragmentCalendarBinding
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import javax.inject.Inject

class CalendarFragment : BaseBindingFragment<FragmentCalendarBinding>() {
    private val viewModel: CalendarViewModel by fragmentViewModel()

    @Inject
    internal lateinit var viewModelFactory: CalendarViewModel.Factory
    @Inject
    internal lateinit var controller: CalendarEpoxyController

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCalendarBinding {
        return FragmentCalendarBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(binding: FragmentCalendarBinding, savedInstanceState: Bundle?) {
        binding.recyclerView.run {
            setHasFixedSize(false)
            setController(controller)
            addOnScrollListener(HideImeOnScrollListener())
        }

        binding.refreshLayout.setOnRefreshListener(viewModel::refresh)

        view?.findViewById<View>(R.id.searchBar)?.apply {
            with(findViewById<SearchView>(R.id.searchView)) {
                queryHint = getString(R.string.calendar_search_hint)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        hideSoftInput()
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        viewModel.setSearchQuery(newText)
                        return true
                    }
                })
            }
            doOnApplyWindowInsets { view, insets, initialState ->
                binding.recyclerView.updatePadding(top = insets.systemWindowInsetTop + dp(84))
                view.updatePadding(left = insets.systemWindowInsetLeft + initialState.paddings.left,
                        right = insets.systemWindowInsetRight + initialState.paddings.left,
                        top = insets.systemWindowInsetTop + initialState.paddings.top + dp(8))
            }

            doOnSizeChange {
                binding.refreshLayout.setProgressViewOffset(true,
                        0,
                        it.height + binding.refreshLayout.progressCircleDiameter / 2
                )
                true
            }
        }
    }

    override fun invalidate(binding: FragmentCalendarBinding) = withState(viewModel) { state ->
        binding.state = state
        controller.state = state
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controller.cancelPendingModelBuild()
        controller.clear()
    }
}