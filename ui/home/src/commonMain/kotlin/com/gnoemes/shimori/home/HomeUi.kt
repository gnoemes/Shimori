@file:OptIn(ExperimentalMaterialApi::class)

package com.gnoemes.shimori.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalLogger
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.NestedScaffold
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.compose.ui.PersonCover
import com.gnoemes.shimori.common.compose.ui.ShimoriSnackbar
import com.gnoemes.shimori.common.compose.ui.UiMessage
import com.gnoemes.shimori.common.ui.navigator.LocalNavigator
import com.gnoemes.shimori.common.ui.navigator.ShimoriNavigator
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_bookmark
import com.gnoemes.shimori.common.ui.resources.icons.ic_explore
import com.gnoemes.shimori.common.ui.resources.icons.ic_profile
import com.gnoemes.shimori.common.ui.resources.icons.ic_search
import com.gnoemes.shimori.common.ui.resources.icons.ic_settings
import com.gnoemes.shimori.common.ui.resources.strings.explore
import com.gnoemes.shimori.common.ui.resources.strings.lists_title
import com.gnoemes.shimori.common.ui.resources.strings.profile
import com.gnoemes.shimori.common.ui.resources.strings.search
import com.gnoemes.shimori.common.ui.resources.strings.settings
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.screens.AuthScreen
import com.gnoemes.shimori.screens.ExploreScreen
import com.gnoemes.shimori.screens.HomeScreen
import com.gnoemes.shimori.screens.MockScreen
import com.gnoemes.shimori.screens.SearchScreen
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
    val windowSizeClass = LocalWindowSizeClass.current.widthSizeClass
    val isCompact by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.isCompact()
        }
    }

    val navigationItems by remember(state, isCompact) {
        derivedStateOf {
            buildNavigationItems(
                isAuthorized = state.isAuthorized,
                extendedNavigation = !isCompact,
                profileImage = state.profileImage
            )
        }
    }

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
        state.fabSpacing,
        state.message,
        onMessageShown = { eventSink(HomeUiEvent.ClearMessage(it)) },
        onMessageAction = { eventSink(HomeUiEvent.ActionMessage(it)) },
        openSearch = { },
        openSettings = { shimoriNavigator.goTo(SettingsScreen) },
        logout = { eventSink(HomeUiEvent.Logout) }
    )
}

@Composable
private fun HomeUi(
    modifier: Modifier,
    navigationItems: List<HomeNavigationItem>,
    navigator: Navigator,
    backstack: SaveableBackStack,
    fabSpacing: Boolean,
    message: UiMessage?,
    onMessageShown: (Long) -> Unit,
    onMessageAction: (UiMessage) -> Unit,
    openSearch: () -> Unit,
    openSettings: () -> Unit,
    logout: () -> Unit,
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val density = LocalDensity.current
    val navigationType by remember(windowSizeClass) {
        derivedStateOf { NavigationType.forWindowSizeSize(windowSizeClass) }
    }

    val rootScreen by remember(backstack) {
        derivedStateOf { backstack.last().screen }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val dismissSnackbarState = rememberDismissState { value ->
        if (value != DismissValue.Default) {
            snackbarHostState.currentSnackbarData?.dismiss()
            true
        } else {
            false
        }
    }

    message?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(
                it.message,
                it.actionLabel,
                withDismissAction = it.actionLabel != null,
                duration = SnackbarDuration.Long
            )
            onMessageShown(it.id)
        }
    }

    NestedScaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                SwipeToDismiss(
                    state = dismissSnackbarState,
                    background = {},
                    dismissContent = {
                        ShimoriSnackbar(
                            data = data,
                            message = message,
                            dismissAction = { message?.let(onMessageAction) }
                        )
                    },
                    modifier = Modifier.padding(bottom = 24.dp)
                        .padding(horizontal = 8.dp)
                )
            }
        },
        floatingActionButton = {
            //add fab spacing if nested screen contains it
            if (fabSpacing) {
                Box(modifier = Modifier.size(56.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            AnimatedVisibility(
                navigationType == NavigationType.BOTTOM_NAVIGATION,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                HomeNavigationBar(
                    selectedNavigation = rootScreen,
                    navigationItems = navigationItems,
                    onNavigationSelected = {
                        navigator.resetRoot(
                            it,
                            saveState = true,
                            restoreState = true
                        )
                    },
                    onNavigationReSelected = {
                        navigator.resetRootIfDifferent(it, restoreState = true)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                navigationType == NavigationType.RAIL
            ) {
                HomeNavigationRail(
                    selectedNavigation = rootScreen,
                    navigationItems = navigationItems,
                    onNavigationSelected = {
                        //TODO remove. simple auth clean for desktop testing
                        if (it is MockScreen) {
                            logout()
                        }

                        navigator.resetRoot(
                            it,
                            saveState = true,
                            restoreState = true
                        )
                    },
                    onNavigationReSelected = {
                        navigator.resetRootIfDifferent(it, restoreState = true)
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
    onNavigationReSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        Spacer(Modifier.weight(1f))
        for (item in navigationItems) {
            val selected = selectedNavigation == item.screen

            if (item.screen is SettingsScreen) {
                //add spacing before settings
                Spacer(Modifier.weight(1f))
            }

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
                selected = selected,
                onClick = {
                    if (selected) onNavigationReSelected(item.screen)
                    else onNavigationSelected(item.screen)
                },
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HomeNavigationBar(
    selectedNavigation: Screen,
    navigationItems: List<HomeNavigationItem>,
    onNavigationSelected: (Screen) -> Unit,
    onNavigationReSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val surface = MaterialTheme.colorScheme.surface

    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to surface.copy(alpha = 0f),
            .22f to surface.copy(alpha = 0.6f),
            .54f to surface.copy(alpha = 0.9f),
            1f to surface,
        ),
        tileMode = TileMode.Clamp
    )

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
                val selected = selectedNavigation == item.screen
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    selected = selected,
                    onClick = {
                        if (selected) onNavigationReSelected(item.screen)
                        else onNavigationSelected(item.screen)
                    },
                    icon = {
                        if (item is HomeNavigationItem.ImageNavigationItem && item.image != null) {
                            PersonCover(image = item.image)
                        } else {
                            HomeNavigationItemIcon(
                                item = item,
                                selected = selected
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
    extendedNavigation: Boolean,
    profileImage: ShimoriImage?,
): List<HomeNavigationItem> {
    return mutableListOf(
        HomeNavigationItem.IconNavigationItem(
            screen = if (isAuthorized) TracksScreen else AuthScreen,
            label = Strings.lists_title,
            contentDescription = Strings.lists_title,
            iconImageResource = Icons.ic_bookmark
        ),
        HomeNavigationItem.IconNavigationItem(
            screen = ExploreScreen(),
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
    ).apply {
        if (extendedNavigation) {
            add(
                HomeNavigationItem.IconNavigationItem(
                    screen = SearchScreen,
                    label = Strings.search,
                    contentDescription = Strings.search,
                    iconImageResource = Icons.ic_search
                )
            )

            add(
                HomeNavigationItem.IconNavigationItem(
                    screen = SettingsScreen,
                    label = Strings.settings,
                    contentDescription = Strings.settings,
                    iconImageResource = Icons.ic_settings
                )
            )
        }
    }
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
