package com.gnoemes.shimori.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.ui.PersonCover
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_explore
import com.gnoemes.shimori.common.ui.resources.icons.ic_list
import com.gnoemes.shimori.common.ui.resources.icons.ic_profile
import com.gnoemes.shimori.common.ui.resources.strings.explore
import com.gnoemes.shimori.common.ui.resources.strings.lists_title
import com.gnoemes.shimori.common.ui.resources.strings.profile
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.screens.AuthScreen
import com.gnoemes.shimori.screens.ListsScreen
import com.gnoemes.shimori.screens.MockScreen
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.isAtRoot
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Home(
    backStack: SaveableBackStack,
    navigator: Navigator,
    isAuthorized: Boolean,
    profileImage: ShimoriImage?,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val navigationType = remember(windowSizeClass) {
        NavigationType.forWindowSizeSize(windowSizeClass)
    }

    val rootScreen by remember(backStack) {
        derivedStateOf { backStack.last().screen }
    }

    val navigationItems = remember(isAuthorized, profileImage) { buildNavigationItems(isAuthorized, profileImage) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
                HomeNavigationBar(
                    selectedNavigation = rootScreen,
                    navigationItems = navigationItems,
                    onNavigationSelected = { navigator.resetRootIfDifferent(it, backStack) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            ContentWithOverlays(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                NavigableCircuitContent(
                    navigator = navigator,
                    backStack = backStack,
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
private fun HomeNavigationBar(
    selectedNavigation: Screen,
    navigationItems: List<HomeNavigationItem>,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                        .22f to MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        .54f to MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        1f to MaterialTheme.colorScheme.surface,
                    ),
                    tileMode = TileMode.Clamp
                )
            )
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
            else -> PERMANENT_DRAWER
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
            screen = if (isAuthorized) ListsScreen else AuthScreen,
            label = Strings.lists_title,
            contentDescription = Strings.lists_title,
            iconImageResource = Icons.ic_list
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
    backstack: SaveableBackStack,
) {
    if (!backstack.isAtRoot || backstack.topRecord?.screen != screen) {
        resetRoot(screen)
    }
}
