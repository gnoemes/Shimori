package com.gnoemes.shimori.main

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.NavController
import com.airbnb.mvrx.viewModel
import com.gnoemes.common.BaseActivity
import com.gnoemes.common.extensions.setupWithNavController
import com.gnoemes.shimori.R
import com.gnoemes.shimori.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()

    @Inject
    lateinit var mainNavigationViewModelFactory: MainViewModel.Factory

    private lateinit var binding: ActivityMainBinding

    private var currentNavController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.mainRoot.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if (savedInstanceState == null) {
            setupBottomNavigation()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        setupBottomNavigation()
    }


    private fun setupBottomNavigation() {
        binding.bottomNav.setupWithNavController(
                listOf(
                        R.navigation.rate_navigation,
                        R.navigation.calendar_navigation,
                        R.navigation.search_navigation,
                        R.navigation.news_navigation,
                        R.navigation.profile_navigation
                ),
                supportFragmentManager,
                R.id.fragmentContainer,
                intent
        ).observe(this) { navController ->
            currentNavController = navController

        }
    }
}
