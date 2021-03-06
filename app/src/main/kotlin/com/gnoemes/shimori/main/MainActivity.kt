package com.gnoemes.shimori.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.common.BaseActivity
import com.gnoemes.shimori.common.compose.LocalShimoriRateUtil
import com.gnoemes.shimori.common.compose.theme.ShimoriTheme
import com.gnoemes.shimori.common.extensions.shouldUseDarkColors
import com.gnoemes.shimori.common.utils.ShimoriRateUtil
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    @Inject
    internal lateinit var prefs: ShimoriPreferences
    @Inject
    internal lateinit var rateUtil: ShimoriRateUtil


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setContent {
            CompositionLocalProvider(
                    LocalElevationOverlay provides null,
                    LocalShimoriRateUtil provides rateUtil
            ) {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    ShimoriTheme(useDarkColors = prefs.shouldUseDarkColors()) {
                        Main()
                    }
                }
            }
        }

    }
}