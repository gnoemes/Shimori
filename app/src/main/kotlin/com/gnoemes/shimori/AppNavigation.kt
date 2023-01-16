@file:OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)

package com.gnoemes.shimori

import ListsEdit
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.*
import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shimori.auth.Auth
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.lists.Lists
import com.gnoemes.shimori.lists.change.ListsChangeSheet
import com.gnoemes.shimori.settings.Settings
import com.gnoemes.shimori.title.TitleDetails
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import soup.compose.material.motion.animation.*
import soup.compose.material.motion.navigation.MaterialMotionNavHost
import soup.compose.material.motion.navigation.composable

internal sealed class RootScreen(val route: String) {
    abstract fun getStartDestination(): Screen

    object Lists : RootScreen("listsroot") {
        override fun getStartDestination() = Screen.Lists
    }

    object Explore : RootScreen("exploreroot") {
        override fun getStartDestination() = Screen.Explore
    }

    object Feed : RootScreen("feedroot") {
        override fun getStartDestination() = Screen.Feed
    }
}

internal sealed class Screen(val route: String) {
    fun createRoute(root: RootScreen) = "${root.route}/$route"

    object Lists : Screen("lists")
    object ListsChangeSheet : Screen("lists_change_sheet")
    object ListsEditSheet :
        Screen("lists_edit?targetId={id}&targetType={type}&markComplete={markComplete}&deleteNotification={deleteNotification}") {
        fun createRoute(
            root: RootScreen,
            id: Long,
            type: TrackTargetType,
            markComplete: Boolean,
            deleteNotification: Boolean,
        ): String {
            return "${root.route}/lists_edit?targetId=$id&targetType=$type&markComplete=$markComplete&deleteNotification=$deleteNotification"
        }
    }

    object Explore : Screen("explore")
    object Feed : Screen("feed")
    object Profile : Screen("profile")
    object Search : Screen("search")

    object Settings : Screen("settings")

    object TitleDetails : Screen("title?id={id}&type={type}") {
        fun createRoute(
            root: RootScreen,
            id: Long,
            type: TrackTargetType,
        ): String {
            return "${root.route}/title?id=$id&type=$type"
        }
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    bottomSheetNavigateUp: () -> Unit
) {
    MaterialMotionNavHost(
        navController = navController,
        startDestination = RootScreen.Lists.route,
        enterTransition = { enterTransition() },
        exitTransition = { exitTransition() },
    ) {
        addListsRoot(navController, bottomSheetNavigateUp)
        addExploreRoot(navController)
        addFeedRoot(navController)
    }
}

private fun NavGraphBuilder.addListsRoot(
    navController: NavController, bottomSheetNavigateUp: () -> Unit
) {
    navigation(
        route = RootScreen.Lists.route,
        startDestination = Screen.Lists.createRoute(RootScreen.Lists)
    ) {
        val root = RootScreen.Lists
        addLists(navController, root)
        addListChangeBottomSheet(navController, root, bottomSheetNavigateUp)
        addListEditBottomSheet(navController, root, bottomSheetNavigateUp)

        addSettings(navController, root)
        addTitleDetails(navController, root)
    }
}

private fun NavGraphBuilder.addExploreRoot(
    navController: NavController
) {
    navigation(
        route = RootScreen.Explore.route,
        startDestination = Screen.Explore.createRoute(RootScreen.Explore)
    ) {
        addExplore(navController, RootScreen.Explore)
    }
}

private fun NavGraphBuilder.addFeedRoot(
    navController: NavController
) {
    navigation(
        route = RootScreen.Feed.route,
        startDestination = Screen.Feed.createRoute(RootScreen.Feed)
    ) {
        addFeed(navController, RootScreen.Feed)
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
private fun NavGraphBuilder.addLists(navController: NavController, root: RootScreen) {
    composable(
        route = Screen.Lists.createRoute(root),
    ) {
        val shikimori: Shikimori by localDI().instance()
        val state by shikimori.authState.collectAsStateWithLifecycle()

        if (!state.isAuthorized) {
            Auth(openSettings = { navController.navigate(Screen.Settings.createRoute(root)) })
        } else {
            Lists(
//                openUser = { navController.navigate(Screen.Explore.createRoute(root)) },
                openUser = { navController.navigate(Screen.Settings.createRoute(root)) },
                openSearch = { navController.navigate(Screen.Search.createRoute(root)) },
                openListsEdit = { id, type, markComplete ->
                    navController.navigate(
                        Screen.ListsEditSheet.createRoute(
                            root,
                            id,
                            type,
                            markComplete,
                            true
                        )
                    )
                },
                openTitleDetails = { id, type ->
                    navController.navigate(
                        Screen.TitleDetails.createRoute(
                            root,
                            id,
                            type
                        )
                    )
                },
                onAnimeExplore = {
                    //TODO navigation
                },
                onMangaExplore = {
                    //TODO navigation
                },
                onRanobeExplore = {
                    //TODO navigation
                },
                onChangeList = {
                    navController.navigate(Screen.ListsChangeSheet.createRoute(RootScreen.Lists))
                })
        }
    }
}

private fun NavGraphBuilder.addExplore(
    navController: NavController, root: RootScreen
) {
    composable(
        route = Screen.Explore.createRoute(root),
    ) {
        MockScreen(Screen.Explore.createRoute(root))
    }
}

private fun NavGraphBuilder.addFeed(
    navController: NavController, root: RootScreen
) {
    composable(
        route = Screen.Feed.createRoute(root),
    ) {
        MockScreen(Screen.Feed.createRoute(root))
    }
}

private fun NavGraphBuilder.addListChangeBottomSheet(
    navController: NavController,
    root: RootScreen,
    bottomSheetNavigateUp: () -> Unit
) {
    bottomSheet(
        Screen.ListsChangeSheet.createRoute(root),
        bottomSheetNavigateUp,
    ) {
        ListsChangeSheet(
            navigateUp = bottomSheetNavigateUp
        )
    }
}

private fun NavGraphBuilder.addListEditBottomSheet(
    navController: NavController,
    root: RootScreen,
    bottomSheetNavigateUp: () -> Unit
) {
    bottomSheet(
        Screen.ListsEditSheet.createRoute(root),
        bottomSheetNavigateUp,
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
            navArgument("type") { type = NavType.EnumType(TrackTargetType::class.java) },
            navArgument("markComplete") { type = NavType.BoolType },
            navArgument("deleteNotification") { type = NavType.BoolType },
        ),
    ) {
        val bottomSheetNavigator = try {
            navController.navigatorProvider.getNavigator(BottomSheetNavigator::class.java)
        } catch (e: Exception) {
            null
        }
        //used to snap bottom bar
        val offset = remember {
            bottomSheetNavigator?.navigatorSheetState?.offset ?: mutableStateOf(0f)
        }

        ListsEdit(
            bottomSheetOffset = offset,
            navigateUp = bottomSheetNavigateUp
        )
    }
}

private fun NavGraphBuilder.addSettings(
    navController: NavController,
    root: RootScreen,
) {
    composable(
        route = Screen.Settings.createRoute(root),
    ) {
        Settings(navigateUp = { navController.navigateUp() })
    }
}

private fun NavGraphBuilder.addTitleDetails(
    navController: NavController,
    root: RootScreen,
) {
    composable(
        route = Screen.TitleDetails.createRoute(root),
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
            navArgument("type") { type = NavType.EnumType(TrackTargetType::class.java) },
        ),
    ) {
        TitleDetails(
            navigateUp = { navController.navigateUp() },
            openListsEdit = { id, type, markComplete ->
                navController.navigate(
                    Screen.ListsEditSheet.createRoute(
                        root,
                        id,
                        type,
                        markComplete,
                        deleteNotification = false
                    )
                )
            },
            openCharacterDetails = {
                //TODO
            },
            openCharacterList = { id, type ->
                //TODO
            }
        )
    }
}

private fun NavGraphBuilder.bottomSheet(
    route: String,
    bottomSheetNavigateUp: () -> Unit,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable ColumnScope.(backstackEntry: NavBackStackEntry) -> Unit
) {
    addDestination(
        BottomSheetNavigator.Destination(
            provider[BottomSheetNavigator::class],
            content = {
                val bottomSheetBackPressedCallback = remember {
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            bottomSheetNavigateUp()
                        }
                    }
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                val backDispatcher =
                    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

                DisposableEffect(lifecycleOwner) {
                    //Set bottom sheet callback to current back dispatcher (Activity)
                    backDispatcher?.apply {
                        addCallback(lifecycleOwner, bottomSheetBackPressedCallback)
                    }

                    onDispose {
                        bottomSheetBackPressedCallback.remove()
                    }
                }


                content(it)
            }
        ).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}

@Composable
private fun MockScreen(
    text: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        val defaultText = "REPLACE ME"
        Text(text = text?.let { "$defaultText\n#$text" } ?: defaultText,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Right))
    }
}

private fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition {
    val isCurrentMainSection = isMainSectionScreen(initialState.destination.route)
    val isTargetMainSection = isMainSectionScreen(targetState.destination.route)
    return when {
        isCurrentMainSection && isTargetMainSection -> materialFadeThroughIn()
        isCurrentMainSection -> holdIn()
        else -> materialSharedAxisZIn(true)
    }
}

private fun AnimatedContentScope<NavBackStackEntry>.exitTransition(): ExitTransition {
    val isCurrentMainSection = isMainSectionScreen(initialState.destination.route)
    val isTargetMainSection = isMainSectionScreen(targetState.destination.route)
    return when {
        isCurrentMainSection && isTargetMainSection -> materialFadeThroughOut()
        isTargetMainSection -> holdOut()
        else -> materialSharedAxisZOut(false)
    }
}

private fun isMainSectionScreen(route: String?): Boolean {
    return when (route) {
        Screen.Lists.route,
        Screen.Explore.route,
        Screen.Feed.route
        -> true
        else -> false
    }
}