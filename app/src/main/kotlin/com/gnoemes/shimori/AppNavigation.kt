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
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.gnoemes.shimori.lists.Lists

internal sealed class RootScreen(val route: String) {
    abstract fun getStartDestination(): Screen

    object Lists : RootScreen("listsroot") {
        override fun getStartDestination() = Screen.Lists
    }

    object Explore : RootScreen("exploreroot") {
        override fun getStartDestination() = Screen.Explore
    }

    object Forum : RootScreen("forumroot") {
        override fun getStartDestination() = Screen.Forum
    }

    object Conversations : RootScreen("conversationsroot") {
        override fun getStartDestination() = Screen.Conversations
    }
}

internal sealed class Screen(val route: String) {
    object Lists : Screen("lists")

    object Explore : Screen("explore")
    object Forum : Screen("forum")
    object Conversations : Screen("conversations")

    object Profile : Screen("profile")
    object Search : Screen("search")
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
        addForumRoot(navController)
        addConversationsRoot(navController)
    }
}

private fun NavGraphBuilder.addListsRoot(
    navController: NavController
) {
    navigation(
            route = RootScreen.Lists.route,
            startDestination = Screen.Lists.route
    ) {
        addLists(navController)
        addExplore(navController)
    }
}

private fun NavGraphBuilder.addExploreRoot(
    navController: NavController
) {
    navigation(
            route = RootScreen.Explore.route,
            startDestination = Screen.Explore.route
    ) {
        addExplore(navController)
    }
}

private fun NavGraphBuilder.addForumRoot(
    navController: NavController
) {
    navigation(
            route = RootScreen.Forum.route,
            startDestination = Screen.Forum.route
    ) {
        addForum(navController)
    }
}

private fun NavGraphBuilder.addConversationsRoot(
    navController: NavController
) {
    navigation(
            route = RootScreen.Conversations.route,
            startDestination = Screen.Conversations.route
    ) {
        addConversations(navController)
    }
}


private fun NavGraphBuilder.addLists(navController: NavController) {
    composable(
            Screen.Lists.route,
    ) {
        Lists(
                openUser = { navController.navigate(Screen.Explore.route) },
                openSearch = { navController.navigate(Screen.Search.route) },
        )
    }
}

private fun NavGraphBuilder.addExplore(navController: NavController) {
    composable(Screen.Explore.route) {
        MockScreen(Screen.Explore.route)
    }
}

private fun NavGraphBuilder.addForum(navController: NavController) {
    composable(Screen.Forum.route) {
        MockScreen(Screen.Forum.route)
    }
}

private fun NavGraphBuilder.addConversations(navController: NavController) {
    composable(Screen.Conversations.route) {
        MockScreen(Screen.Conversations.route)
    }
}


@Composable
private fun MockScreen(
    text: String? = null
) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
    ) {
        val defaultText = "REPLACE ME"
        Text(text = text?.let { "$defaultText\n#$text" }
            ?: defaultText, modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.h6.copy(textAlign = TextAlign.Right))
    }
}