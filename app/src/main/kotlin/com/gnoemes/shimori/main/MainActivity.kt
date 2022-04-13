package com.gnoemes.shimori.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.gnoemes.shikimori.ShikimoriManager
import com.gnoemes.shimori.base.settings.ShimoriSettings
import com.gnoemes.shimori.common.BaseActivity
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.compose.theme.ShimoriTheme
import com.gnoemes.shimori.common.compose.theme.defaultDimensions
import com.gnoemes.shimori.common.compose.theme.sw360Dimensions
import com.gnoemes.shimori.common.extensions.shouldUseDarkColors
import com.gnoemes.shimori.common.utils.ShimoriRateUtil
import com.gnoemes.shimori.common.utils.ShimoriTextCreator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    @Inject
    internal lateinit var settings: ShimoriSettings

    @Inject
    internal lateinit var rateUtil: ShimoriRateUtil

    @Inject
    internal lateinit var textCreator: ShimoriTextCreator

    @Inject
    internal lateinit var shikimori: ShikimoriManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {

            val dimensions =
                if (LocalConfiguration.current.screenWidthDp <= 360) defaultDimensions
                else sw360Dimensions

            val shikimoriAuth = shikimori.state.collectAsState().value

            CompositionLocalProvider(
                LocalShimoriRateUtil provides rateUtil,
                LocalShimoriTextCreator provides textCreator,
                LocalShimoriSettings provides settings,
                LocalShimoriDimensions provides dimensions,
                LocalShikimoriAuth provides shikimoriAuth
            ) {

                val useDarkColors = settings.shouldUseDarkColors()

                ShimoriTheme(useDarkColors = useDarkColors) {

                    val systemUiController = rememberSystemUiController()
                    val isLightTheme = !useDarkColors
                    val navigationColor = MaterialTheme.colorScheme.background.copy(alpha = 0.96f)
                    val statusBarColor = MaterialTheme.colorScheme.background.copy(alpha = 0.96f)

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

                    Main()
                }
            }
        }
    }
}