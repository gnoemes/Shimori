package com.gnoemes.shimori.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalLogger
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.ui.PersonCover
import com.gnoemes.shimori.common.ui.navigator.LocalNavigator
import com.gnoemes.shimori.common.ui.navigator.ShimoriNavigator
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_bookmark
import com.gnoemes.shimori.common.ui.resources.icons.ic_explore
import com.gnoemes.shimori.common.ui.resources.icons.ic_profile
import com.gnoemes.shimori.common.ui.resources.strings.explore
import com.gnoemes.shimori.common.ui.resources.strings.lists_title
import com.gnoemes.shimori.common.ui.resources.strings.profile
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.screens.AuthScreen
import com.gnoemes.shimori.screens.HomeScreen
import com.gnoemes.shimori.screens.MockScreen
import com.gnoemes.shimori.screens.SettingsScreen
import com.gnoemes.shimori.screens.TracksScreen
import com.gnoemes.shimori.screens.UrlScreen
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.popUntil
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@CircuitInject(screen = HomeScreen::class, UiScope::class)
internal fun HomeUi(
    state: HomeUiState,
    modifier: Modifier = Modifier
) {
    val backstack = rememberSaveableBackStack(TracksScreen)
    val navigator = rememberCircuitNavigator(backstack) { /* no-op */ }

    val logger = LocalLogger.current
    val eventSink = state.eventSink

    val shimoriNavigator: Navigator = remember(navigator) {
        ShimoriNavigator(
            navigator,
            backstack,
            onOpenUrl = {
                eventSink(HomeUiEvent.OnNavEvent(NavEvent.GoTo(UrlScreen(it))))
                true
            },
            logger
        )
    }

    val navigationItems =
        remember(state) { buildNavigationItems(state.isAuthorized, state.profileImage) }

    //update root for lists tab by auth
    LaunchedEffect(state.isAuthorized) {
        if (state.isAuthorized) navigator.resetRoot(
            TracksScreen,
            saveState = true,
            restoreState = true
        )
        else navigator.resetRoot(AuthScreen, saveState = true, restoreState = true)
    }

    HomeUi(
        modifier,
        navigationItems,
        shimoriNavigator,
        backstack,
        openSearch = { },
        openSettings = { shimoriNavigator.goTo(SettingsScreen) },
    )
}

@Composable
private fun HomeUi(
    modifier: Modifier,
    navigationItems: List<HomeNavigationItem>,
    navigator: Navigator,
    backstack: SaveableBackStack,
    openSearch: () -> Unit,
    openSettings: () -> Unit,
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val navigationType = remember(windowSizeClass) {
        NavigationType.forWindowSizeSize(windowSizeClass)
    }

    val rootScreen by remember(backstack) {
        derivedStateOf { backstack.last().screen }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                navigationType == NavigationType.BOTTOM_NAVIGATION,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                HomeNavigationBar(
                    backStackSize = backstack.size,
                    selectedNavigation = rootScreen,
                    navigationItems = navigationItems,
                    onNavigationSelected = {
                        navigator.resetRoot(
                            it,
                            saveState = true,
                            restoreState = true
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                navigationType == NavigationType.RAIL
            ) {
                HomeNavigationRail(
                    selectedNavigation = rootScreen,
                    navigationItems = navigationItems,
                    onNavigationSelected = {
                        navigator.resetRoot(
                            it,
                            saveState = true,
                            restoreState = true
                        )
                    },
                    modifier = Modifier.fillMaxHeight(),
                )
            }
            CompositionLocalProvider(LocalNavigator provides navigator) {
                NavigableCircuitContent(
                    navigator = navigator,
                    backStack = backstack,
                    decoration = remember(navigator) {
                        GestureNavigationDecoration(onBackInvoked = navigator::pop)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun HomeNavigationRail(
    selectedNavigation: Screen,
    navigationItems: List<HomeNavigationItem>,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        Spacer(Modifier.weight(1f))
        for (item in navigationItems) {
            NavigationRailItem(
                icon = {
                    if (item is HomeNavigationItem.ImageNavigationItem && item.image != null) {
                        PersonCover(image = item.image)
                    } else {
                        HomeNavigationItemIcon(
                            item = item,
                            selected = selectedNavigation == item.screen
                        )
                    }
                },
                alwaysShowLabel = true,
                modifier = Modifier.width(80.dp)
                    .heightIn(max = 56.dp)
                    .fillMaxHeight(),
                label = { Text(text = stringResource(item.label)) },
                selected = selectedNavigation == item.screen,
                onClick = { onNavigationSelected(item.screen) },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun HomeNavigationBar(
    backStackSize: Int,
    selectedNavigation: Screen,
    navigationItems: List<HomeNavigationItem>,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val surface = MaterialTheme.colorScheme.surface
    val surfaceContainer = MaterialTheme.colorScheme.surfaceContainer

    val color by animateColorAsState(
        if (backStackSize == 1) surface
        else surfaceContainer
    )
    val brush = if (backStackSize == 1) Brush.verticalGradient(
        colorStops = arrayOf(
            0f to color.copy(alpha = 0f),
            .22f to color.copy(alpha = 0.6f),
            .54f to color.copy(alpha = 0.9f),
            1f to color,
        ),
        tileMode = TileMode.Clamp
    )
    else SolidColor(color)

    Box(
        modifier = modifier
            .background(brush = brush)
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            windowInsets = WindowInsets.navigationBars
        ) {
            for (item in navigationItems) {
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    selected = selectedNavigation == item.screen,
                    onClick = { onNavigationSelected(item.screen) },
                    icon = {
                        if (item is HomeNavigationItem.ImageNavigationItem && item.image != null) {
                            PersonCover(image = item.image)
                        } else {
                            HomeNavigationItemIcon(
                                item = item,
                                selected = selectedNavigation == item.screen
                            )
                        }
                    },
                    label = { Text(text = stringResource(item.label)) },
                )
            }
        }
    }

}

@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    if (item is HomeNavigationItem.IconNavigationItem && item.selectedImageResource != null) {
        Crossfade(targetState = selected) { s ->
            Icon(
                painter = painterResource(if (s) item.selectedImageResource else item.iconImageResource),
                contentDescription = stringResource(item.contentDescription),
            )
        }
    } else {
        Icon(
            painter = painterResource(item.iconImageResource),
            contentDescription = stringResource(item.contentDescription),
        )
    }
}

internal enum class NavigationType {
    BOTTOM_NAVIGATION,
    RAIL,
    PERMANENT_DRAWER, ;

    companion object {
        fun forWindowSizeSize(windowSizeClass: WindowSizeClass): NavigationType = when {
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact -> BOTTOM_NAVIGATION
            windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact -> BOTTOM_NAVIGATION
            windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium -> RAIL
            else -> RAIL
        }
    }
}

@Immutable
private abstract class HomeNavigationItem {
    abstract val screen: Screen
    abstract val label: StringResource
    abstract val contentDescription: StringResource
    abstract val iconImageResource: DrawableResource

    data class IconNavigationItem(
        override val screen: Screen,
        override val label: StringResource,
        override val contentDescription: StringResource,
        override val iconImageResource: DrawableResource,
        val selectedImageResource: DrawableResource? = null,
    ) : HomeNavigationItem()

    data class ImageNavigationItem(
        override val screen: Screen,
        override val label: StringResource,
        override val contentDescription: StringResource,
        override val iconImageResource: DrawableResource,
        val image: ShimoriImage?,
    ) : HomeNavigationItem()
}

private fun buildNavigationItems(
    isAuthorized: Boolean,
    profileImage: ShimoriImage?
): List<HomeNavigationItem> {
    return listOf(
        HomeNavigationItem.IconNavigationItem(
            screen = if (isAuthorized) TracksScreen else AuthScreen,
            label = Strings.lists_title,
            contentDescription = Strings.lists_title,
            iconImageResource = Icons.ic_bookmark
        ),
        HomeNavigationItem.IconNavigationItem(
            screen = MockScreen,
            label = Strings.explore,
            contentDescription = Strings.explore,
            iconImageResource = Icons.ic_explore
        ),
        HomeNavigationItem.ImageNavigationItem(
            screen = MockScreen,
            label = Strings.profile,
            contentDescription = Strings.profile,
            iconImageResource = Icons.ic_profile,
            image = profileImage
        )
    )
}

private fun Navigator.resetRootIfDifferent(
    screen: Screen,
    saveState: Boolean = false,
    restoreState: Boolean = false,
) {
    val backStack = peekBackStack()

    if (backStack.lastOrNull() == screen) {
        Snapshot.withMutableSnapshot {
            popUntil { peekBackStack().size == 1 }
        }
    } else {
        resetRoot(screen, saveState, restoreState)
    }
}
