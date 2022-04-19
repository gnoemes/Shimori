import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

//package com.gnoemes.shimori.main
//
//import androidx.activity.OnBackPressedCallback
//import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
//import androidx.annotation.DrawableRes
//import androidx.annotation.StringRes
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.ModalBottomSheetValue
//import androidx.compose.material.SwipeableDefaults
//import androidx.compose.material.rememberModalBottomSheetState
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.util.fastForEach
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import androidx.navigation.NavDestination.Companion.hierarchy
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.compose.rememberNavController
//import com.gnoemes.shimori.AppNavigation
//import com.gnoemes.shimori.R
//import com.gnoemes.shimori.RootScreen
//import com.gnoemes.shimori.common.compose.LocalShimoriDimensions
//import com.gnoemes.shimori.common_ui.compose.components.ShimoriBottomBarItem
//import com.gnoemes.shimori.common.extensions.rememberStateWithLifecycle
//import com.gnoemes.shimori.model.rate.ListType
//import com.google.accompanist.navigation.material.BottomSheetNavigator
//import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
//import com.google.accompanist.navigation.material.ModalBottomSheetLayout
//import kotlinx.coroutines.launch

@Composable
internal fun Main() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        val defaultText = "REPLACE ME"
        Text(
            text = defaultText,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Right))
    }
}


//@Composable
//internal fun Main() {
//    Main(viewModel = hiltViewModel())
//}
//
//@OptIn(
//    ExperimentalMaterialNavigationApi::class,
//    ExperimentalMaterial3Api::class,
//    androidx.compose.material.ExperimentalMaterialApi::class
//)
//@Composable
//internal fun Main(
//    viewModel: MainViewModel
//) {
//    //Create custom sheet state with skip half behavior
//    val sheetState = rememberModalBottomSheetState(
//        ModalBottomSheetValue.Hidden,
//        SwipeableDefaults.AnimationSpec,
//        skipHalfExpanded = true
//    )
//
//    //Create bottom sheet navigator & nav controller
//    val bottomSheetNavigator = remember(sheetState) {
//        BottomSheetNavigator(sheetState = sheetState)
//    }
//    val navController = rememberNavController(bottomSheetNavigator)
//
//    //Close bottom sheets smoothly with state.hide() instead of navController.navigateUp()
//    val scope = rememberCoroutineScope()
//    val bottomSheetNavigateUp: () -> Unit = { scope.launch { sheetState.hide() } }
//    val bottomSheetBackPressedCallback = remember {
//        object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (sheetState.isVisible) {
//                    bottomSheetNavigateUp()
//                }
//            }
//        }
//    }
//
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
//    navController.setLifecycleOwner(lifecycleOwner)
//
//    //Set bottom sheet callback to current back dispatcher (Activity)
//    backDispatcher?.apply {
//        addCallback(lifecycleOwner, bottomSheetBackPressedCallback)
//    }?.let(navController::setOnBackPressedDispatcher)
//
//    DisposableEffect(lifecycleOwner) {
//        onDispose {
//            bottomSheetBackPressedCallback.remove()
//        }
//    }
//
//    val viewState by rememberStateWithLifecycle(viewModel.state)
//
//    ModalBottomSheetLayout(
//        bottomSheetNavigator = bottomSheetNavigator,
//        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
//        sheetContentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface)
//    ) {
//        Scaffold(
//            bottomBar = {
//                MainBottomBar(
//                    viewState = viewState,
//                    navController = navController,
//                )
//            }
//        ) {
//            AppNavigation(
//                navController = navController,
//                bottomSheetNavigateUp = bottomSheetNavigateUp
//            )
//        }
//    }
//}
//
//@Composable
//internal fun MainBottomBar(
//    viewState: MainViewState,
//    navController: NavController,
//) {
//    val currentSelectedItem by navController.currentScreenAsState()
//
//    Column {
//        MainNavigationBar(
//            viewState.listType,
//            selectedNavigation = currentSelectedItem,
//            onNavigationSelected = { selected ->
//
//                navController.navigate(selected.route) {
//                    launchSingleTop = true
//                    restoreState = true
//
//                    popUpTo(navController.graph.findStartDestination().id) {
//                        saveState = true
//
//                    }
//                }
//            },
//            onNavigationReselected = { selected ->
//                navController.popBackStack(
//                    selected.getStartDestination()
//                        .createRoute(RootScreen.Lists), false
//                )
//            },
//        )
//
//        Spacer(modifier = Modifier.navigationBarsPadding())
//    }
//}
//
//@Composable
//internal fun MainNavigationBar(
//    listType: ListType,
//    selectedNavigation: RootScreen,
//    onNavigationSelected: (RootScreen) -> Unit,
//    onNavigationReselected: (RootScreen) -> Unit,
//) {
//    BottomAppBar(
//        tonalElevation = 0.dp,
//        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
//        modifier = Modifier.height(LocalShimoriDimensions.current.bottomBarHeight)
//    ) {
//        MainNavigationItems.fastForEach { item ->
//            val selected = selectedNavigation == item.screen
//            ShimoriBottomBarItem(
//                selected = selected,
//                icon = {
//                    NavigationItemIcon(item = item, selected = selected, listType = listType)
//                },
//                label = {
//                    NavigationItemLabel(item = item, selected = selected, listType = listType)
//                },
//                onClick = {
//                    if (selected) onNavigationReselected(item.screen)
//                    else onNavigationSelected(item.screen)
//                },
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//private fun NavigationItemIcon(item: NavigationItem, selected: Boolean, listType: ListType) {
//    val painter = when (item) {
//        is NavigationItem.ListTypeItem -> painterResource(id = item.iconResId(selected, listType))
//        is NavigationItem.StaticItem -> painterResource(id = item.iconResId)
//    }
//
//    val contentDescription = when (item) {
//        is NavigationItem.ListTypeItem -> stringResource(id = item.labelResId(selected, listType))
//        is NavigationItem.StaticItem -> stringResource(id = item.contentDescriptionResId)
//    }
//
//    Icon(
//        painter = painter,
//        contentDescription = contentDescription
//    )
//}
//
//@Composable
//private fun NavigationItemLabel(item: NavigationItem, selected: Boolean, listType: ListType) {
//    val text = when (item) {
//        is NavigationItem.ListTypeItem -> stringResource(id = item.labelResId(selected, listType))
//        is NavigationItem.StaticItem -> stringResource(id = item.labelResId)
//    }
//
//    Text(
//        text = text,
//        modifier = Modifier,
//    )
//}
//
///**
// * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
// * returned [State] which is updated as the destination changes.
// */
//@Stable
//@Composable
//private fun NavController.currentScreenAsState(): State<RootScreen> {
//    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Lists) }
//
//    DisposableEffect(this) {
//        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
//            when {
//                destination.hierarchy.any { it.route == RootScreen.Lists.route } -> {
//                    selectedItem.value = RootScreen.Lists
//                }
//                destination.hierarchy.any { it.route == RootScreen.Explore.route } -> {
//                    selectedItem.value = RootScreen.Explore
//                }
//                destination.hierarchy.any { it.route == RootScreen.Feed.route } -> {
//                    selectedItem.value = RootScreen.Feed
//                }
//                destination.hierarchy.any { it.route == RootScreen.Talks.route } -> {
//                    selectedItem.value = RootScreen.Talks
//                }
//            }
//        }
//
//        addOnDestinationChangedListener(listener)
//
//        onDispose {
//            removeOnDestinationChangedListener(listener)
//        }
//    }
//
//    return selectedItem
//}
//
//private sealed class NavigationItem(
//    val screen: RootScreen,
//) {
//    class ListTypeItem : NavigationItem(RootScreen.Lists) {
//        @StringRes
//        fun labelResId(selected: Boolean, type: ListType): Int =
//            if (selected) R.string.lists_title
//            else when (type) {
//                ListType.Anime -> R.string.anime
//                ListType.Manga -> R.string.manga
//                ListType.Ranobe -> R.string.ranobe
//                else -> R.string.pinned
//            }
//
//        @DrawableRes
//        fun iconResId(selected: Boolean, type: ListType): Int =
//            if (selected) R.drawable.ic_menu
//            else when (type) {
//                ListType.Anime -> R.drawable.ic_anime
//                ListType.Manga -> R.drawable.ic_manga
//                ListType.Ranobe -> R.drawable.ic_ranobe
//                else -> R.drawable.ic_pin
//            }
//    }
//
//    class StaticItem(
//        screen: RootScreen,
//        @StringRes val labelResId: Int,
//        @StringRes val contentDescriptionResId: Int,
//        @DrawableRes val iconResId: Int,
//    ) : NavigationItem(screen)
//}
//
//private val MainNavigationItems = listOf(
//    NavigationItem.ListTypeItem(),
//    NavigationItem.StaticItem(
//        screen = RootScreen.Explore,
//        labelResId = R.string.explore_title,
//        contentDescriptionResId = R.string.explore_title,
//        iconResId = R.drawable.ic_explore,
//    ),
//    NavigationItem.StaticItem(
//        screen = RootScreen.Feed,
//        labelResId = R.string.feed_title,
//        contentDescriptionResId = R.string.feed_title,
//        iconResId = R.drawable.ic_feed,
//    )
//)