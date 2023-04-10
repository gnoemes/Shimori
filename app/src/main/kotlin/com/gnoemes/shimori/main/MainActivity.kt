package com.gnoemes.shimori.main

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.common.ui.BaseActivity
import com.gnoemes.shimori.common.ui.LocalPreferences
import com.gnoemes.shimori.common.ui.LocalShimoriDimensions
import com.gnoemes.shimori.common.ui.LocalShimoriSettings
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.LocalShimoriTrackUtil
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.theme.ShimoriTheme
import com.gnoemes.shimori.common.ui.theme.defaultDimensions
import com.gnoemes.shimori.common.ui.utils.ShimoriDateTimeFormatter
import com.gnoemes.shimori.common.ui.utils.ShimoriTextCreator
import com.gnoemes.shimori.common.ui.utils.ShimoriTextProvider
import com.gnoemes.shimori.common.ui.utils.ShimoriTrackUtil
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.common.ui.utils.shouldUseDarkColors
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
class MainActivity : BaseActivity(), DIAware {
    override val di: DI by closestDI()

    private val settings: ShimoriSettings by instance()
    private val textProvider: ShimoriTextProvider by instance()
    private val formatter: ShimoriDateTimeFormatter by instance()
    private val trackUtil: ShimoriTrackUtil by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            withDI(di = di) {
//                val dimensions =
//                    if (LocalConfiguration.current.screenWidthDp <= 360) defaultDimensions
//                    else sw360Dimensions
                val dimensions = defaultDimensions

                val viewModel: MainViewModel = shimoriViewModel()

                val settingsState by viewModel.settingsState.collectAsState()

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

                val overscrollConfiguration = OverscrollConfiguration(
                    glowColor = MaterialTheme.colorScheme.onPrimary,
                    drawPadding = PaddingValues(top = 24.dp, bottom = 24.dp)
                )

                CompositionLocalProvider(
                    LocalShimoriTrackUtil provides trackUtil,
                    LocalShimoriTextCreator provides textCreator,
                    LocalShimoriSettings provides settings,
                    LocalShimoriDimensions provides dimensions,
                    LocalPreferences provides prefs,
                    LocalOverscrollConfiguration provides overscrollConfiguration,
                    LocalDensity provides Density(3.75f),
                ) {
                    val useDarkColors = settings.shouldUseDarkColors()

                    ShimoriTheme(useDarkColors = useDarkColors) {
                        val systemUiController = rememberSystemUiController()
                        val useDarkIcons = !useDarkColors
                        val navigationColor = MaterialTheme.colorScheme.background
                        val statusBarColor = Color.Transparent

                        DisposableEffect(systemUiController, useDarkColors, navigationColor) {
                            systemUiController.setStatusBarColor(
                                color = statusBarColor,
                                darkIcons = useDarkIcons
                            )
                            systemUiController.setNavigationBarColor(
                                color = navigationColor,
                                darkIcons = useDarkIcons
                            )

                            onDispose {}
                        }

                        val homeScreen = rememberScreen(FeatureScreen.Home)

                        BottomSheetNavigator(
                            scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = .32f),
                            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            sheetContentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
                            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                        ) {
                            Navigator(screen = homeScreen)
                        }
                    }
                }
            }
        }
    }
}