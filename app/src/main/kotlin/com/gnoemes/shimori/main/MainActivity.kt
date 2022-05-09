package com.gnoemes.shimori.main

import Main
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.common.ui.*
import com.gnoemes.shimori.common.ui.theme.ShimoriTheme
import com.gnoemes.shimori.common.ui.theme.defaultDimensions
import com.gnoemes.shimori.common.ui.theme.sw360Dimensions
import com.gnoemes.shimori.common.ui.utils.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance

class MainActivity : BaseActivity(), DIAware {
    override val di: DI by closestDI()

    private val settings: ShimoriSettings by instance()
    private val textProvider: ShimoriTextProvider by instance()
    private val formatter: ShimoriDateTimeFormatter by instance()
    private val rateUtil: ShimoriRateUtil by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen()

        setContent {
            withDI(di = di) {
                val dimensions =
                    if (LocalConfiguration.current.screenWidthDp <= 360) defaultDimensions
                    else sw360Dimensions

                val viewModel: MainViewModel = shimoriViewModel()

                val settingsState by rememberStateWithLifecycle(viewModel.settingsState)

                val textCreator = settingsState.let { state ->
                    ShimoriTextCreator(
                        textProvider,
                        formatter,
                        state.titlesLocale,
                        state.appLocale
                    )
                }

                val prefs =
                    LocalContext.current.getSharedPreferences("defaults", Context.MODE_PRIVATE)

                CompositionLocalProvider(
                    LocalShimoriRateUtil provides rateUtil,
                    LocalShimoriTextCreator provides textCreator,
                    LocalShimoriSettings provides settings,
                    LocalShimoriDimensions provides dimensions,
                    LocalPreferences provides prefs
                ) {
                    val useDarkColors = settings.shouldUseDarkColors()

                    ShimoriTheme(useDarkColors = useDarkColors) {
                        val systemUiController = rememberSystemUiController()
                        val isLightTheme = !useDarkColors
                        val navigationColor = MaterialTheme.colorScheme.background
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