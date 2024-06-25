package com.gnoemes.shimori.home

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import com.gnoemes.shimori.common.compose.LocalShimoriDateTextFormatter
import com.gnoemes.shimori.common.compose.LocalShimoriIconsUtil
import com.gnoemes.shimori.common.compose.LocalShimoriPreferences
import com.gnoemes.shimori.common.compose.LocalShimoriSettings
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.shouldUseDarkColors
import com.gnoemes.shimori.common.compose.shouldUseDynamicColors
import com.gnoemes.shimori.common.compose.theme.ShimoriTheme
import com.gnoemes.shimori.common.ui.overlays.LocalNavigator
import com.gnoemes.shimori.common.ui.resources.ShimoriIconsUtil
import com.gnoemes.shimori.common.ui.resources.util.ShimoriDateTextFormatter
import com.gnoemes.shimori.common.ui.resources.util.ShimoriTextCreator
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.screens.UrlScreen
import com.gnoemes.shimori.settings.ShimoriSettings
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.retained.LocalRetainedStateRegistry
import com.slack.circuit.retained.continuityRetainedStateRegistry
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Inject

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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalCoilApi::class)
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
            LocalWindowSizeClass provides calculateWindowSizeClass(),
            LocalRetainedStateRegistry provides continuityRetainedStateRegistry()
        ) {
            CircuitCompositionLocals(circuit) {
                ShimoriTheme(
                    useDarkColors = settings.shouldUseDarkColors(),
                    useDynamicColors = settings.shouldUseDynamicColors()
                ) {
                    Home(
                        backStack = backstack,
                        navigator = shimoriNavigator,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

private class ShimoriNavigator(
    private val navigator: Navigator,
    private val backstack: SaveableBackStack,
    private val onOpenUrl: (String) -> Boolean,
    private val logger: Logger,
) : Navigator {

    override fun goTo(screen: Screen): Boolean {
        logger.d { "goTo. Screen: $screen. Current stack: ${backstack.toList()}" }

        if (screen is UrlScreen && onOpenUrl(screen.url)) {
            return true
        }
        return navigator.goTo(screen)
    }

    override fun resetRoot(
        newRoot: Screen,
        saveState: Boolean,
        restoreState: Boolean
    ): ImmutableList<Screen> {
        logger.d { "resetRoot. New root: $newRoot. Current stack: ${backstack.toList()}" }
        return navigator.resetRoot(newRoot)
    }

    override fun pop(result: PopResult?): Screen? {
        logger.d { "pop. Current stack: ${backstack.toList()}" }
        return navigator.pop(result)
    }

    override fun peek(): Screen? = navigator.peek()
    override fun peekBackStack(): ImmutableList<Screen> = navigator.peekBackStack()

}