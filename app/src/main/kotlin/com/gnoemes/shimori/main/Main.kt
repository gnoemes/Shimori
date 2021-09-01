package com.gnoemes.shimori.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.gnoemes.shimori.AppNavigation
import com.gnoemes.shimori.R
import com.gnoemes.shimori.RootScreen
import com.gnoemes.shimori.Screen
import com.gnoemes.shimori.common.compose.ChevronIcon
import com.gnoemes.shimori.common.compose.EndContentBadgedBox
import com.gnoemes.shimori.common.compose.EnlargedButton
import com.gnoemes.shimori.common.compose.Scaffold
import com.gnoemes.shimori.common.compose.theme.caption
import com.gnoemes.shimori.common.compose.theme.toolbar
import com.gnoemes.shimori.model.rate.ListType
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.BottomNavigation
import kotlinx.coroutines.launch


@Composable
internal fun Main() {
    Main(viewModel = hiltViewModel())
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Main(
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val viewState by viewModel.state.collectAsState()

    val submit = { action: MainAction -> viewModel.submitAction(action) }

    val sheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
            bottomBar = {
                MainBoottomBar(
                        viewState = viewState,
                        navController = navController,
                        showBottomSheet = { coroutineScope.launch { sheetState.show() } }
                )
            }
    ) {
        Box(Modifier.fillMaxSize()) {
            AppNavigation(navController = navController)
        }
    }

    val onBottomSheetClick: (MainAction) -> Unit = {
        coroutineScope.launch { sheetState.hide() }
        submit(it)
    }

    RateTypeSelectBottomSheet(
            sheetState = sheetState,
            action = onBottomSheetClick,
            selectedListType = viewState.listType
    )
}

@Composable
internal fun MainBoottomBar(
    viewState: MainViewState,
    navController: NavController,
    showBottomSheet: () -> Unit
) {
    val currentSelectedItem by navController.currentScreenAsState()

    val canShowBottomSheet by navController.canShowListTypeBottomSheetAsState()

    MainBottomNavigation(
            viewState.listType,
            canShowListIcon = canShowBottomSheet,
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

                //show list type select bottom sheet if lists tab was reselected twice
                if (canShowBottomSheet) {
                    showBottomSheet()
                } else {
                    navController.popBackStack(selected.getStartDestination().route, false)
                }
            },
            modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MainBottomNavigation(
    listType: ListType,
    selectedNavigation: RootScreen,
    canShowListIcon: Boolean,
    onNavigationSelected: (RootScreen) -> Unit,
    onNavigationReselected: (RootScreen) -> Unit,
    modifier: Modifier
) {

    BottomNavigation(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.surface,
            contentPadding = rememberInsetsPaddingValues(insets = LocalWindowInsets.current.navigationBars),
            modifier = modifier
    ) {
        MainNavigationItems.fastForEach { item ->
            val selected = selectedNavigation == item.screen
            BottomNavigationItem(
                    icon = {
                        MainNavigationItemIcon(item, selected, listType)
                    },
                    label = {
                        EndContentBadgedBox(badge = {
                            if (item is NavigationItem.ListTypeItem && canShowListIcon) {
                                Icon(
                                        painter = painterResource(id = R.drawable.ic_chevron_up),
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = MaterialTheme.colors.secondary
                                )
                            }
                        }) {
                            MainNavigationItemLabel(item, selected, listType)
                        }
                    },
                    selected = selected,
                    selectedContentColor = MaterialTheme.colors.secondary,
                    unselectedContentColor = MaterialTheme.colors.caption,
                    onClick = {
                        if (selected) onNavigationReselected(item.screen)
                        else onNavigationSelected(item.screen)
                    }
            )
        }
    }
}

@Composable
private fun MainNavigationItemIcon(item: NavigationItem, selected: Boolean, listType: ListType) {
    val painter = when (item) {
        is NavigationItem.ListTypeItem -> painterResource(id = item.iconResId(listType))
        is NavigationItem.StaticItem -> painterResource(id = item.iconResId)
    }

    val contentDescription = when (item) {
        is NavigationItem.ListTypeItem -> stringResource(id = item.labelResId(listType))
        is NavigationItem.StaticItem -> stringResource(id = item.contentDescriptionResId)
    }


    Icon(
            painter = painter,
            contentDescription = contentDescription,
    )
}

@Composable
private fun MainNavigationItemLabel(item: NavigationItem, selected: Boolean, listType: ListType) {
    val text = when (item) {
        is NavigationItem.ListTypeItem -> stringResource(id = item.labelResId(listType))
        is NavigationItem.StaticItem -> stringResource(id = item.labelResId)
    }

    Text(
            text = text,
            style = MaterialTheme.typography.caption,
            modifier = Modifier,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RateTypeSelectBottomSheet(
    sheetState: ModalBottomSheetState,
    action: (MainAction) -> Unit,
    selectedListType: ListType
) {

    ModalBottomSheetLayout(
            sheetContent = {
                val thumbColor = MaterialTheme.colors.caption
                Canvas(modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(4.dp)
                    .width(16.dp)
                    .align(Alignment.CenterHorizontally)
                ) {

                    drawRoundRect(
                            color = thumbColor,
                            cornerRadius = CornerRadius(x = 16f, y = 16f)
                    )
                }

                Text(
                        text = stringResource(id = R.string.titles),
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                            .fillMaxWidth()
                )

                EnlargedButton(
                        selected = false,
                        onClick = { action(MainAction.Random) },
                        painter = painterResource(R.drawable.ic_random),
                        text = stringResource(R.string.open_random_title_from_list),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                            .height(48.dp)
                            .fillMaxWidth()
                ) {
                    ChevronIcon()
                }

                EnlargedButton(
                        selected = selectedListType == ListType.Pinned,
                        onClick = { action(MainAction.ChangeListType(ListType.Pinned)) },
                        painter = painterResource(R.drawable.ic_pin),
                        text = stringResource(R.string.pinned),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                            .height(48.dp)
                            .fillMaxWidth()
                )

                EnlargedButton(
                        selected = selectedListType == ListType.Anime,
                        onClick = { action(MainAction.ChangeListType(ListType.Anime)) },
                        painter = painterResource(R.drawable.ic_anime),
                        text = stringResource(R.string.anime),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                            .height(48.dp)
                            .fillMaxWidth()
                )


                EnlargedButton(
                        selected = selectedListType == ListType.Manga,
                        onClick = { action(MainAction.ChangeListType(ListType.Manga)) },
                        painter = painterResource(R.drawable.ic_manga),
                        text = stringResource(R.string.manga),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                            .height(48.dp)
                            .fillMaxWidth()
                )

                EnlargedButton(
                        selected = selectedListType == ListType.Ranobe,
                        onClick = { action(MainAction.ChangeListType(ListType.Ranobe)) },
                        painter = painterResource(R.drawable.ic_ranobe),
                        text = stringResource(R.string.ranobe),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 24.dp)
                            .height(48.dp)
                            .fillMaxWidth()
                )

                Spacer(modifier = Modifier.navigationBarsHeight())

            },
            scrimColor = MaterialTheme.colors.toolbar,
            sheetState = sheetState,
            sheetElevation = 8.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {

    }
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
                destination.hierarchy.any { it.route == RootScreen.Talks.route } -> {
                    selectedItem.value = RootScreen.Talks
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

@Composable
private fun NavController.canShowListTypeBottomSheetAsState(): State<Boolean> {
    val canShow = remember { mutableStateOf(false) }

    DisposableEffect("canShowBottomSheet") {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            canShow.value = destination.route == Screen.Lists.route
        }

        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return canShow
}

private sealed class NavigationItem(
    val screen: RootScreen,
) {
    class ListTypeItem : NavigationItem(RootScreen.Lists) {
        @StringRes
         fun labelResId(type: ListType): Int =  when (type) {
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
        ),
        NavigationItem.StaticItem(
                screen = RootScreen.Talks,
                labelResId = R.string.talks_title,
                contentDescriptionResId = R.string.talks_title,
                iconResId = R.drawable.ic_conversation,
        ),
)