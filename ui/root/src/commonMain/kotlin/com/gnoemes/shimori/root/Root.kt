package com.gnoemes.shimori.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalLogger
import com.gnoemes.shimori.common.compose.LocalShimoriDateTextFormatter
import com.gnoemes.shimori.common.compose.LocalShimoriIconsUtil
import com.gnoemes.shimori.common.compose.LocalShimoriPreferences
import com.gnoemes.shimori.common.compose.LocalShimoriSettings
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.calculateWindowSizeClass
import com.gnoemes.shimori.common.compose.shouldUseDarkColors
import com.gnoemes.shimori.common.compose.shouldUseDynamicColors
import com.gnoemes.shimori.common.compose.theme.ShimoriTheme
import com.gnoemes.shimori.common.ui.overlay.LocalNavigator
import com.gnoemes.shimori.common.ui.overlay.ShimoriNavigator
import com.gnoemes.shimori.common.ui.resources.ShimoriIconsUtil
import com.gnoemes.shimori.common.ui.resources.util.ShimoriDateTextFormatter
import com.gnoemes.shimori.common.ui.resources.util.ShimoriTextCreator
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.settings.ShimoriSettings
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.retained.LocalRetainedStateRegistry
import com.slack.circuit.retained.continuityRetainedStateRegistry
import com.slack.circuit.runtime.Navigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

interface ShimoriContent {
    @Composable
    fun Content(
        backstack: SaveableBackStack,
        navigator: Navigator,
        onOpenUrl: (String) -> Boolean,
        modifier: Modifier,
    )
}

@Inject
@SingleIn(UiScope::class)
@ContributesBinding(UiScope::class)
class DefaultShimoriContent(
    private val rootViewModel: (CoroutineScope) -> RootViewModel,
    private val circuit: Circuit,
    private val textCreator: ShimoriTextCreator,
    private val dateFormatter: ShimoriDateTextFormatter,
    private val preferences: ShimoriPreferences,
    private val settings: ShimoriSettings,
    private val iconsUtil: ShimoriIconsUtil,
    private val imageLoader: ImageLoader,
    private val logger: Logger,
) : ShimoriContent {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun Content(
        backstack: SaveableBackStack,
        navigator: Navigator,
        onOpenUrl: (String) -> Boolean,
        modifier: Modifier
    ) {
        val coroutineScope = rememberCoroutineScope()
        remember { rootViewModel(coroutineScope) }

        val shimoriNavigator: Navigator = remember(navigator) {
            ShimoriNavigator(navigator, backstack, onOpenUrl, logger)
        }

        setSingletonImageLoaderFactory { imageLoader }

        CompositionLocalProvider(
            LocalNavigator provides shimoriNavigator,
            LocalShimoriIconsUtil provides iconsUtil,
            LocalShimoriTextCreator provides textCreator,
            LocalShimoriDateTextFormatter provides dateFormatter,
            LocalShimoriPreferences provides preferences,
            LocalShimoriSettings provides settings,
            LocalLogger provides logger,
            LocalWindowSizeClass provides calculateWindowSizeClass(),
            LocalRetainedStateRegistry provides continuityRetainedStateRegistry()
        ) {
            CircuitCompositionLocals(circuit) {
                ShimoriTheme(
                    useDarkColors = settings.shouldUseDarkColors(),
                    useDynamicColors = settings.shouldUseDynamicColors()
                ) {
                    ContentWithOverlays(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        NavigableCircuitContent(
                            navigator = shimoriNavigator,
                            backStack = backstack,
                            decoration = remember(shimoriNavigator) {
                                GestureNavigationDecoration(onBackInvoked = shimoriNavigator::pop)
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

