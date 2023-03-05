package com.mclowicz.testfriends.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mclowicz.testfriends.presentation.friends.FriendsScreen
import com.mclowicz.testfriends.presentation.postComposer.CreateNewPostScreen
import com.mclowicz.testfriends.presentation.signup.SignUpScreen
import com.mclowicz.testfriends.presentation.timeline.TimelineScreen

@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                onSignUp = { userId ->
                    navController.navigate(Screen.Home.route + "/${userId}") {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "${Screen.Home.route}/{userId}",
            arguments = listOf(navArgument("userId") { })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            HomeScreen(userId = userId)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    userId: String
) {
    val navigationController = rememberNavController()
    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val screens = listOf(Screen.Main.Timeline, Screen.Main.Friends)
    val shouldShowBottomNav = screens.any { it.route == currentDestination }
    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav) {
                HomeScreenBottomNavigation(navigationController)
            }
        },
        content = {
            NavHost(
                navController = navigationController,
                startDestination = Screen.Main.Timeline.route,
                modifier = Modifier.padding(bottom = 50.dp)
            ) {
                composable(
                    route = Screen.Main.Timeline.route,
                ) {
                    TimelineScreen(
                        userId = userId,
                        onCreateNewPost = {
                            navigationController.navigate(Screen.CreateNewPost.route)
                        }
                    )
                }
                composable(route = Screen.CreateNewPost.route) {
                    CreateNewPostScreen(
                        onPostCreated = {
                            navigationController.navigateUp()
                        }
                    )
                }
                composable(route = Screen.Main.Friends.route) {
                    FriendsScreen(
                        userId = userId
                    )
                }
            }
        }
    )
}

@Composable
fun HomeScreenBottomNavigation(
    navController: NavController
) {
    val screens = listOf(Screen.Main.Timeline, Screen.Main.Friends)
    BottomNavigation() {
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconResId),
                        contentDescription = null
                    )
                },
                label = { Text(text = screen.title) },
                selected = false,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}