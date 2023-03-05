package com.mclowicz.testfriends.navigation

import com.mclowicz.testfriends.R

sealed class Screen(val title: String, val route: String) {
    object SignUp : Screen(title = "SignUp", route = "signup_screen")
    object Home : Screen(title = "Home", route = "home_screen")
    object CreateNewPost : Screen(title = "Create new post", route = "create_new_post_screen")
    sealed class Main(val title: String, val route: String, val iconResId: Int) {
        object Timeline : Main(
            title = "Timeline",
            route = "timeline_screen",
            iconResId = R.drawable.ic_timeline
        )
        object Friends : Main(
            title = "Friends",
            route = "friends_screen",
            iconResId = R.drawable.ic_friends
        )
    }
}