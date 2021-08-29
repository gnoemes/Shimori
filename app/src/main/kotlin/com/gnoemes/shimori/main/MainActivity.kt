package com.gnoemes.shimori.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.common.BaseActivity
import com.gnoemes.shimori.common.compose.LocalShimoriDimensions
import com.gnoemes.shimori.common.compose.LocalShimoriRateUtil
import com.gnoemes.shimori.common.compose.LocalShimoriSettings
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.theme.ShimoriTheme
import com.gnoemes.shimori.common.compose.theme.defaultDimensions
import com.gnoemes.shimori.common.compose.theme.sw360Dimensions
import com.gnoemes.shimori.common.extensions.shouldUseDarkColors
import com.gnoemes.shimori.common.utils.ShimoriRateUtil
import com.gnoemes.shimori.common.utils.ShimoriTextCreator
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    @Inject
    internal lateinit var prefs: ShimoriPreferences

    @Inject
    internal lateinit var rateUtil: ShimoriRateUtil

    @Inject
    internal lateinit var textCreator: ShimoriTextCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setContent {

            val dimensions =
                if (LocalConfiguration.current.screenWidthDp <= 360) defaultDimensions
                else sw360Dimensions

            CompositionLocalProvider(
                    LocalElevationOverlay provides null,
                    LocalShimoriRateUtil provides rateUtil,
                    LocalShimoriTextCreator provides textCreator,
                    LocalShimoriSettings provides prefs,
                    LocalShimoriDimensions provides dimensions
            ) {

                ShimoriTheme(useDarkColors = prefs.shouldUseDarkColors()) {

                    val systemUiController = rememberSystemUiController()
                    val isLightTheme = MaterialTheme.colors.isLight
                    val navigationColor = MaterialTheme.colors.surface.copy(alpha = 0.01f)
                    val statusBarColor = MaterialTheme.colors.primary

                    SideEffect {
                        systemUiController.setStatusBarColor(
                                color = statusBarColor,
                                darkIcons = isLightTheme
                        )
                        systemUiController.setNavigationBarColor(
                                color = navigationColor,
                                darkIcons = isLightTheme
                        )
                    }

                    ProvideWindowInsets(consumeWindowInsets = false) {
                        Main()
                    }
                }
            }
        }
    }
}