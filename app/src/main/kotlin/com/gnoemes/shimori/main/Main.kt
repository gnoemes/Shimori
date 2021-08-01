package com.gnoemes.shimori.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.gnoemes.shimori.common.compose.EnlargedButton
import com.gnoemes.shimori.common.compose.LocalShimoriRateUtil
import com.gnoemes.shimori.common.compose.Scaffold
import com.gnoemes.shimori.common.compose.ShimoriIconButton
import com.gnoemes.shimori.common.compose.theme.caption
import com.gnoemes.shimori.common.compose.theme.subInfoStyle
import com.gnoemes.shimori.common.compose.theme.toolbar
import com.gnoemes.shimori.model.rate.RateTargetType
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.CoroutineScope
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

    var openBottomSheet by remember { mutableStateOf(false) }

    val actioner = { action: MainAction ->
        when (action) {
            MainAction.RateTypeDialog -> openBottomSheet = true
            MainAction.RateTypeDialogClosed -> openBottomSheet = false
            else -> viewModel.submitAction(action)
        }
    }

    Scaffold(
            bottomBar = {
                val currentSelectedItem by navController.currentScreenAsState()

                val canShowBottomSheet by navController.canShowListTypeBottomSheetAsState()

                MainBottomNavigation(
                        viewState,
                        canShowListIcon = canShowBottomSheet,
                        selectedScreen = currentSelectedItem,
                        onScreenSelected = { selected ->

                            navController.navigate(selected.route) {
                                launchSingleTop = true
                                restoreState = true

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true

                                }
                            }
                        },
                        onScreenReSelected = { selected ->

                            //show list type select bottom sheet if lists tab was reselected twice
                            if (canShowBottomSheet) {
                                openBottomSheet = true
                            } else {
                                navController.popBackStack(selected.getStartDestination().route, false)
                            }
                        },
                )

            }
    ) {
        Box(Modifier.fillMaxSize()) {
            AppNavigation(
                    navController = navController,
            )
        }
    }

    if (openBottomSheet) {
        val sheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                confirmStateChange = { value ->
                    when (value) {
                        ModalBottomSheetValue.Expanded -> true
                        ModalBottomSheetValue.HalfExpanded -> false
                        ModalBottomSheetValue.Hidden -> {
                            actioner(MainAction.RateTypeDialogClosed)
                            true
                        }
                    }
                },
        )

        val coroutineScope = rememberCoroutineScope()


        LaunchedEffect(key1 = viewModel) {
            launch { sheetState.show() }
        }

        RateTypeSelectBottomSheet(coroutineScope = coroutineScope, action = actioner, selectedRateType = viewState.rateTargetType, sheetState = sheetState)
    }
}

@Composable
internal fun MainBottomNavigation(
    viewState: MainViewState,
    selectedScreen: RootScreen,
    canShowListIcon: Boolean,
    onScreenSelected: (RootScreen) -> Unit,
    onScreenReSelected: (RootScreen) -> Unit
) {

    Surface(
            color = MaterialTheme.colors.surface,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsHeight(additional = 56.dp)
                .navigationBarsPadding(start = false, end = false)
    ) {
        Row(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
        ) {


            RateSelectionNavigationItem(
                    viewState.rateTargetType,
                    canShowListIcon = canShowListIcon,
                    selected = selectedScreen == RootScreen.Lists,
                    onClick = { onScreenSelected(RootScreen.Lists) },
                    onReselect = { onScreenReSelected(RootScreen.Lists) }
            )

            MainBottomNavigationItem(
                    selected = selectedScreen == RootScreen.Explore,
                    stringResource(R.string.explore_title),
                    painter = painterResource(R.drawable.ic_explore),
                    onClick = {
                        val screen = RootScreen.Explore
                        if (selectedScreen == screen) onScreenReSelected(screen)
                        else onScreenSelected(screen)
                    }
            )

            MainBottomNavigationItem(
                    selected = selectedScreen == RootScreen.Forum,
                    stringResource(R.string.forum_title),
                    painter = painterResource(R.drawable.ic_feed),
                    onClick = {
                        val screen = RootScreen.Forum
                        if (selectedScreen == screen) onScreenReSelected(screen)
                        else onScreenSelected(screen)
                    }
            )

            MainBottomNavigationItem(
                    selected = selectedScreen == RootScreen.Conversations,
                    stringResource(R.string.conversations_title),
                    painter = painterResource(R.drawable.ic_conversation),
                    onClick = {
                        val screen = RootScreen.Conversations
                        if (selectedScreen == screen) onScreenReSelected(screen)
                        else onScreenSelected(screen)
                    }
            )
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun RowScope.RateSelectionNavigationItem(
    selectedRateType: RateTargetType,
    selected: Boolean,
    canShowListIcon: Boolean,
    onClick: () -> Unit,
    onReselect: () -> Unit
) {

    EnlargedButton(
            selected = selected,
            onClick = if (selected) onReselect else onClick,
            modifier = Modifier
                .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                .height(32.dp)
                .widthIn(min = 152.dp, max = 400.dp)
                .weight(1f),
            painter = painterResource(id = LocalShimoriRateUtil.current.rateTargetTypeIcon(selectedRateType)),
            text = LocalShimoriRateUtil.current.rateTargetTypeName(selectedRateType),
            textStyle = MaterialTheme.typography.subInfoStyle
    ) {

        AnimatedVisibility(
                visible = selected && canShowListIcon,
                enter = fadeIn(initialAlpha = 0.4f),
                exit = fadeOut(animationSpec = tween(durationMillis = 250))

        ) {
            Icon(
                    painter = painterResource(R.drawable.ic_unfold),
                    contentDescription = stringResource(R.string.lists_title),
                    tint = if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary,
                    modifier = Modifier.wrapContentWidth(Alignment.End)
            )
        }
    }
}

@Composable
private fun MainBottomNavigationItem(
    selected: Boolean,
    contentDescription: String,
    painter: Painter,
    onClick: () -> Unit
) {
    ShimoriIconButton(onClick = onClick,
            selected = selected,
            painter = painter,
            modifier = Modifier
                .padding(12.dp)
                .size(32.dp),
            contentDescription = contentDescription
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RateTypeSelectBottomSheet(
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    action: (MainAction) -> Unit,
    selectedRateType: RateTargetType
) {

    val bottomSheetOnClick = { childAction: () -> Unit ->
        childAction()
        coroutineScope.launch {
            //Manual close doesn't trigger confirmStateChange
            sheetState.hide()
            action(MainAction.RateTypeDialogClosed)
        }
    }

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
                        onClick = { bottomSheetOnClick { action(MainAction.Random) } },
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
                        selected = selectedRateType.anime,
                        onClick = { bottomSheetOnClick { action(MainAction.ChangeRateType(RateTargetType.ANIME)) } },
                        painter = painterResource(R.drawable.ic_anime),
                        text = stringResource(R.string.anime),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                            .height(48.dp)
                            .fillMaxWidth()
                )


                EnlargedButton(
                        selected = selectedRateType == RateTargetType.MANGA,
                        onClick = { bottomSheetOnClick { action(MainAction.ChangeRateType(RateTargetType.MANGA)) } },
                        painter = painterResource(R.drawable.ic_manga),
                        text = stringResource(R.string.manga),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                            .height(48.dp)
                            .fillMaxWidth()
                )

                EnlargedButton(
                        selected = selectedRateType == RateTargetType.RANOBE,
                        onClick = { bottomSheetOnClick { action(MainAction.ChangeRateType(RateTargetType.RANOBE)) } },
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
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
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
                destination.hierarchy.any { it.route == RootScreen.Forum.route } -> {
                    selectedItem.value = RootScreen.Forum
                }
                destination.hierarchy.any { it.route == RootScreen.Conversations.route } -> {
                    selectedItem.value = RootScreen.Conversations
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