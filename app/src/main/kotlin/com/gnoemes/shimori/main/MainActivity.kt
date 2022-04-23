package com.gnoemes.shimori.main

import Main
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.common.ui.BaseActivity
import com.gnoemes.shimori.common.ui.LocalShimoriDimensions
import com.gnoemes.shimori.common.ui.LocalShimoriSettings
import com.gnoemes.shimori.common.ui.theme.ShimoriTheme
import com.gnoemes.shimori.common.ui.theme.defaultDimensions
import com.gnoemes.shimori.common.ui.theme.sw360Dimensions
import com.gnoemes.shimori.common.ui.utils.shouldUseDarkColors
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance

class MainActivity : BaseActivity(), DIAware {
    override val di: DI by closestDI()

    private val settings: ShimoriSettings by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen()

        setContent {
            withDI(di = di) {
                val dimensions =
                    if (LocalConfiguration.current.screenWidthDp <= 360) defaultDimensions
                    else sw360Dimensions

                CompositionLocalProvider(
//                LocalShimoriRateUtil provides rateUtil,
//                LocalShimoriTextCreator provides textCreator,
                    LocalShimoriSettings provides settings,
                    LocalShimoriDimensions provides dimensions,
//                LocalShikimoriAuth provides shikimoriAuth
                ) {

                    val useDarkColors = settings.shouldUseDarkColors()

                    ShimoriTheme(useDarkColors = useDarkColors) {

                        val systemUiController = rememberSystemUiController()
                        val isLightTheme = !useDarkColors
                        val navigationColor =
                            MaterialTheme.colorScheme.background
                        val statusBarColor =
                            MaterialTheme.colorScheme.background.copy(alpha = 0.96f)

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
}