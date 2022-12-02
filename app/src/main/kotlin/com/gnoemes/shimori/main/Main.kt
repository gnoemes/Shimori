import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.gnoemes.shimori.AppNavigation
import com.gnoemes.shimori.R
import com.gnoemes.shimori.RootScreen
import com.gnoemes.shimori.common.ui.components.ShimoriNavigationBarItem
import com.gnoemes.shimori.common.ui.empty
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.main.MainViewModel
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import kotlinx.coroutines.launch
import soup.compose.material.motion.navigation.rememberMaterialMotionNavController


@Composable
internal fun Main() {
    Main(
        viewModel = shimoriViewModel()
    )
}

@OptIn(
    ExperimentalMaterialNavigationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class
)
@Composable
private fun Main(
    viewModel: MainViewModel
) {
    //Create custom sheet state with skip half behavior
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        SwipeableDefaults.AnimationSpec,
        skipHalfExpanded = true
    )

    //Create bottom sheet navigator & nav controller
    val bottomSheetNavigator = remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
    val navController = rememberMaterialMotionNavController(bottomSheetNavigator)

    //Close bottom sheets smoothly with state.hide() instead of navController.navigateUp()
    val scope = rememberCoroutineScope()
    val bottomSheetNavigateUp: () -> Unit = {
        scope.launch {
            sheetState.hide()
        }
    }

    val viewState by viewModel.state.collectAsState()

    Main(
        listType = viewState.listType,
        bottomSheetNavigator = bottomSheetNavigator,
        navController = navController,
        bottomSheetNavigateUp = bottomSheetNavigateUp
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@ExperimentalMaterialNavigationApi
@Composable
private fun Main(
    listType: ListType,
    bottomSheetNavigator: BottomSheetNavigator,
    navController: NavHostController,
    bottomSheetNavigateUp: () -> Unit,
) {
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
        scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = .32f)
    ) {
        Scaffold(
            bottomBar = {
                MainBottomBar(
                    listType = listType,
                    navController = navController,
                )
            },
            contentWindowInsets = WindowInsets.empty
        ) {
            AppNavigation(
                navController = navController,
                bottomSheetNavigateUp = bottomSheetNavigateUp
            )
        }
    }
}

@Composable
private fun MainBottomBar(
    listType: ListType,
    navController: NavController,
) {
    val currentSelectedItem by navController.currentScreenAsState()

    Column {
        MainNavigationBar(
            listType,
            selectedNavigation = currentSelectedItem,
            onNavigationSelected = { selected ->

                navController.navigate(selected.route) {
                    launchSingleTop = true
                    restoreState = true

                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true

                    }
                }
            },
            onNavigationReselected = { selected ->
                navController.popBackStack(
                    selected.getStartDestination()
                        .createRoute(RootScreen.Lists), false
                )
            },
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun MainNavigationBar(
    listType: ListType,
    selectedNavigation: RootScreen,
    onNavigationSelected: (RootScreen) -> Unit,
    onNavigationReselected: (RootScreen) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.bottomBarContainerHeight)
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to MaterialTheme.colorScheme.background.copy(alpha = 0f),
                        0.52f to MaterialTheme.colorScheme.background.copy(alpha = 0.60f),
                        1f to MaterialTheme.colorScheme.background,
                    ),
                    tileMode = TileMode.Clamp
                )
            )
    ) {
        NavigationBar(
            tonalElevation = 0.dp,
            containerColor = Color.Transparent,
            modifier = Modifier
                .height(MaterialTheme.dimens.bottomBarHeight)
                .align(Alignment.BottomStart),
            windowInsets = WindowInsets.empty,
        ) {
            MainNavigationItems.fastForEach { item ->
                val selected = selectedNavigation == item.screen
                ShimoriNavigationBarItem(
                    selected = selected,
                    icon = {
                        NavigationItemIcon(item = item, listType = listType)
                    },
                    label = {
                        NavigationItemLabel(item = item, listType = listType)
                    },
                    onClick = {
                        if (selected) onNavigationReselected(item.screen)
                        else onNavigationSelected(item.screen)
                    },
                )
            }
        }
    }
}

@Composable
private fun NavigationItemIcon(item: NavigationItem, listType: ListType) {
    val painter = when (item) {
        is NavigationItem.ListTypeItem -> painterResource(id = item.iconResId(listType))
        is NavigationItem.StaticItem -> painterResource(id = item.iconResId)
        else -> null
    } ?: return

    val contentDescription = when (item) {
        is NavigationItem.ListTypeItem -> stringResource(id = item.labelResId(listType))
        is NavigationItem.StaticItem -> stringResource(id = item.contentDescriptionResId)
        else -> null
    }

    Icon(
        painter = painter,
        contentDescription = contentDescription
    )
}

@Composable
private fun NavigationItemLabel(item: NavigationItem, listType: ListType) {
    val text = when (item) {
        is NavigationItem.ListTypeItem -> stringResource(id = item.labelResId(listType))
        is NavigationItem.StaticItem -> stringResource(id = item.labelResId)
        else -> null
    } ?: return

    Text(
        text = text,
        modifier = Modifier,
    )
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Lists) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == RootScreen.Lists.route } -> {
                    selectedItem.value = RootScreen.Lists
                }
                destination.hierarchy.any { it.route == RootScreen.Explore.route } -> {
                    selectedItem.value = RootScreen.Explore
                }
                destination.hierarchy.any { it.route == RootScreen.Feed.route } -> {
                    selectedItem.value = RootScreen.Feed
                }
            }
        }

        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

private sealed class NavigationItem(
    val screen: RootScreen,
) {
    class ListTypeItem : NavigationItem(RootScreen.Lists) {
        @StringRes
        fun labelResId(type: ListType): Int = when (type) {
            ListType.Anime -> R.string.anime
            ListType.Manga -> R.string.manga
            ListType.Ranobe -> R.string.ranobe
            else -> R.string.pinned
        }

        @DrawableRes
        fun iconResId(type: ListType): Int = when (type) {
            ListType.Anime -> R.drawable.ic_anime
            ListType.Manga -> R.drawable.ic_manga
            ListType.Ranobe -> R.drawable.ic_ranobe
            else -> R.drawable.ic_pin
        }
    }

    class StaticItem(
        screen: RootScreen,
        @StringRes val labelResId: Int,
        @StringRes val contentDescriptionResId: Int,
        @DrawableRes val iconResId: Int,
    ) : NavigationItem(screen)
}

private val MainNavigationItems = listOf(
    NavigationItem.ListTypeItem(),
    NavigationItem.StaticItem(
        screen = RootScreen.Explore,
        labelResId = R.string.explore_title,
        contentDescriptionResId = R.string.explore_title,
        iconResId = R.drawable.ic_explore,
    ),
    NavigationItem.StaticItem(
        screen = RootScreen.Feed,
        labelResId = R.string.feed_title,
        contentDescriptionResId = R.string.feed_title,
        iconResId = R.drawable.ic_feed,
    )
)