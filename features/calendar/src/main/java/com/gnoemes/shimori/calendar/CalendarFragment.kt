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
import com.gnoemes.common.ui.widgets.ShimoriSearchView
import com.gnoemes.shimori.calendar.databinding.FragmentCalendarBinding
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.android.synthetic.main.fragment_calendar.*
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
            setController(controller)
            addOnScrollListener(HideImeOnScrollListener())
        }

        binding.refreshLayout.setOnRefreshListener(viewModel::refresh)

        searchBar?.apply {
            with(findViewById<ShimoriSearchView>(R.id.searchView)) {
                setIcon(R.drawable.ic_search)
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
                view.updatePadding(top = insets.systemWindowInsetTop + initialState.paddings.top + dp(8))
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