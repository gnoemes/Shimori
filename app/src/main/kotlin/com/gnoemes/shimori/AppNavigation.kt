@file:OptIn(ExperimentalMaterialNavigationApi::class)

package com.gnoemes.shimori

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gnoemes.shimori.auth.Auth
import com.gnoemes.shimori.common.compose.LocalShikimoriAuth
import com.gnoemes.shimori.lists.Lists
import com.gnoemes.shimori.lists_change.ListsChangeSheet
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.settings.Settings
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

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

    object Talks : RootScreen("talksroot") {
        override fun getStartDestination() = Screen.Talks
    }
}

internal sealed class Screen(private val route: String) {
    fun createRoute(root: RootScreen) = "${root.route}/$route"

    object Lists : Screen("lists")
    object ListsChangeSheet : Screen("lists_change_sheet")
    object ListsEditSheet : Screen("edit?targetId={id}&targetType={type}") {
        fun createRoute(root: RootScreen, id: Long, type: RateTargetType): String {
            return "${root.route}/edit?targetId=$id&targetType=$type"
        }
    }

    object Explore : Screen("explore")
    object Feed : Screen("feed")
    object Talks : Screen("talks")

    object Profile : Screen("profile")
    object Search : Screen("search")

    object Settings : Screen("settings")

}

@Composable
internal fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RootScreen.Lists.route
    ) {
        addListsRoot(navController)
        addExploreRoot(navController)
        addFeedRoot(navController)
        addTalksRoot(navController)
    }
}

private fun NavGraphBuilder.addListsRoot(
    navController: NavController
) {
    navigation(
        route = RootScreen.Lists.route,
        startDestination = Screen.Lists.createRoute(RootScreen.Lists)
    ) {
        val root = RootScreen.Lists

        addLists(navController, root)
        addListChangeBottomSheet(navController, root)
        addListEditBottomSheet(navController, root)

        addSettings(navController, root)
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

private fun NavGraphBuilder.addTalksRoot(
    navController: NavController
) {
    navigation(
        route = RootScreen.Talks.route,
        startDestination = Screen.Talks.createRoute(RootScreen.Talks)
    ) {
        addTalks(navController, RootScreen.Talks)
    }
}

private fun NavGraphBuilder.addLists(navController: NavController, root: RootScreen) {
    composable(
        Screen.Lists.createRoute(root),
    ) {

        if (!LocalShikimoriAuth.current.isAuthorized) {
            Auth(
                openSettings = { navController.navigate(Screen.Settings.createRoute(root)) }
            )
        } else {
            Lists(
                openUser = { navController.navigate(Screen.Explore.createRoute(root)) },
                openSearch = { navController.navigate(Screen.Search.createRoute(root)) },
                openListsEdit = { id, type ->
                    navController.navigate(Screen.ListsEditSheet.createRoute(root, id, type))
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
                }
            )
        }
    }
}

private fun NavGraphBuilder.addExplore(
    navController: NavController,
    root: RootScreen
) {
    composable(Screen.Explore.createRoute(root)) {
        MockScreen(Screen.Explore.createRoute(root))
    }
}

private fun NavGraphBuilder.addFeed(
    navController: NavController,
    root: RootScreen
) {
    composable(Screen.Feed.createRoute(root)) {
        MockScreen(Screen.Feed.createRoute(root))
    }
}

private fun NavGraphBuilder.addTalks(
    navController: NavController,
    root: RootScreen
) {
    composable(Screen.Talks.createRoute(root)) {
        MockScreen(Screen.Talks.createRoute(root))
    }
}

private fun NavGraphBuilder.addListChangeBottomSheet(
    navController: NavController,
    root: RootScreen
) {
    bottomSheet(Screen.ListsChangeSheet.createRoute(root)) {
        ListsChangeSheet(
            navigateUp = { navController.navigateUp() }
        )
    }
}

private fun NavGraphBuilder.addListEditBottomSheet(
    navController: NavController,
    root: RootScreen
) {
    bottomSheet(
        route = Screen.ListsEditSheet.createRoute(root),
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
            navArgument("type") {
                type = NavType.EnumType(RateTargetType::class.java)
            },
        )
    ) {
//        ListsEdit(
//                navigateUp = { navController.navigateUp() }
//        )
    }
}

private fun NavGraphBuilder.addSettings(
    navController: NavController,
    root: RootScreen,
) {
    composable(Screen.Settings.createRoute(root)) {
        Settings(
            navigateUp = { navController.navigateUp() }
        )
    }
}


@Composable
private fun MockScreen(
    text: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        val defaultText = "REPLACE ME"
        Text(text = text?.let { "$defaultText\n#$text" }
            ?: defaultText,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.h6.copy(textAlign = TextAlign.Right))
    }
}