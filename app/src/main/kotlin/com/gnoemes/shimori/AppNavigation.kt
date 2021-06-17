package com.gnoemes.shimori

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

internal sealed class Screen(val route : String) {
    object Lists : Screen("listsroot")
    object Explore : Screen("exploreroot")
    object Forum : Screen("forumroot")
    object Conversations : Screen("conversationsroot")
}

@Composable
internal fun AppNavigation(
    navController: NavHostController,
) {
//    NavHost(
//            navController = navController,
//            startDestination = Screen.Lists.route
//    ) {
//        //TODO
//    }
}