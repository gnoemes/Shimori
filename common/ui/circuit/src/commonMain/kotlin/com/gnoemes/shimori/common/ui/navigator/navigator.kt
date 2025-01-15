package com.gnoemes.shimori.common.ui.navigator

import androidx.compose.runtime.staticCompositionLocalOf
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.screens.UrlScreen
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList

val LocalNavigator = staticCompositionLocalOf<Navigator> { Navigator.NoOp }


class ShimoriNavigator(
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