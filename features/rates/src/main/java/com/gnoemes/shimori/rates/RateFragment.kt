package com.gnoemes.shimori.rates

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.gnoemes.common.BaseBindingFragment
import com.gnoemes.common.extensions.doOnSizeChange
import com.gnoemes.common.extensions.dp
import com.gnoemes.common.extensions.hideSoftInput
import com.gnoemes.common.ui.recyclerview.HideImeOnScrollListener
import com.gnoemes.common.utils.RateUtils
import com.gnoemes.shimori.model.ShikimoriContentEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.app.RateSort
import com.gnoemes.shimori.model.common.ContentType
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.rates.databinding.FragmentRateBinding
import com.gnoemes.shimori.rates.edit.RateEditDialogFragmentDirections
import com.gnoemes.shimori.rates.sort.RateSortDialogFragment
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.android.synthetic.main.fragment_rate.*
import javax.inject.Inject

class RateFragment : BaseBindingFragment<FragmentRateBinding>() {
    private val viewModel: RateViewModel by fragmentViewModel()

    @Inject
    internal lateinit var viewModelFactory: RateViewModel.Factory

    @Inject
    internal lateinit var controller: RateEpoxyController

    companion object {
        private const val DRAWER_KEY = "DRAWER_KEY"
    }

    override fun createBinding(inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle?
    ): FragmentRateBinding {
        return FragmentRateBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(binding: FragmentRateBinding, savedInstanceState: Bundle?) {
        with(binding) {
            recyclerView.run {
                setController(controller)
                addOnScrollListener(HideImeOnScrollListener())
                setItemSpacingRes(R.dimen.spacing_small)
                doOnApplyWindowInsets { view, insets, initialState ->
                    view.updatePadding(top = insets.systemWindowInsetTop + initialState.paddings.top)
                }
            }

            drawer.run {
                val toggle = ActionBarDrawerToggle(
                        activity, drawer, view?.findViewById(R.id.toolbar), R.string.rate_drawer_open, R.string.rate_drawer_close)

                addDrawerListener(toggle)
                setViewScale(Gravity.START, 0.9f)
                setRadius(Gravity.START, 35f)
                setViewElevation(Gravity.START, 20f)
                toggle.syncState()

                savedInstanceState?.let {
                    this.post {
                        toggleDrawer(it.getBoolean(DRAWER_KEY))
                    }
                }
            }

            navView.run {
                findViewById<View>(R.id.design_navigation_view)
                    .updateLayoutParams<FrameLayout.LayoutParams> {
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        gravity = Gravity.CENTER_VERTICAL
                    }
                setNavigationItemSelectedListener {
                    viewModel.submitAction(RateAction.ChangeCategory(RateUtils.fromPriority(it.itemId)))
                    toggleDrawer(false)
                    false
                }
                doOnApplyWindowInsets { _, insets, initialState ->
                    binding.drawer.updatePadding(
                            top = insets.systemWindowInsetTop + initialState.paddings.top,
                            bottom = insets.systemWindowInsetBottom + initialState.paddings.bottom
                    )
                }
            }

            rateSearch.run {
                with(searchView) {
                    setIcon(null)
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

                with(root) {
                    doOnApplyWindowInsets { view, insets, initialState ->
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

            refreshLayout.setOnRefreshListener { viewModel.submitAction(RateAction.Refresh) }
            authLayout.run {
                signUpBtn.setOnClickListener { viewModel.submitAction(RateAction.Auth(true)) }
                signInBtn.setOnClickListener { viewModel.submitAction(RateAction.Auth(false)) }
            }
        }

        controller.callbacks = object : RateEpoxyController.Callbacks {
            override fun onItemClicked(id: Long, type: ContentType) {
            }

            override fun onSortClicked() {
                withState(viewModel) {
                    showRateSortDialog(it.type, it.sort)
                }
            }

            override fun onOrderChangeClicked() {
                viewModel.submitAction(RateAction.ChangeOrder)
            }

            override fun onEditRate(id: Long, name: String, entity: ShimoriEntity) {
                withState(viewModel) { state ->
                    val targetId =
                        if (entity is ShikimoriContentEntity) entity.shikimoriId ?: 0
                        else 0
                    findNavController()
                        .navigate(RateEditDialogFragmentDirections.actionRateEdit(id, state.type, name, targetId))
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(DRAWER_KEY, drawer?.isDrawerOpen(GravityCompat.START) ?: false)
    }

    private fun toggleDrawer(open: Boolean) {
        requireBinding().drawer.run {
            if (isDrawerOpen(GravityCompat.START) == open) return

            if (open) openDrawer(GravityCompat.START)
            closeDrawer(GravityCompat.START)
        }
    }

    private fun showRateSortDialog(type: RateTargetType, sort: RateSort) {
        val dialog = RateSortDialogFragment.newInstance(type, sort)
        dialog.show(childFragmentManager, "SortDialog")
    }

    override fun invalidate(binding: FragmentRateBinding) = withState(viewModel) { state ->
        binding.state = state
        controller.state = state
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controller.cancelPendingModelBuild()
        controller.clear()
    }
}