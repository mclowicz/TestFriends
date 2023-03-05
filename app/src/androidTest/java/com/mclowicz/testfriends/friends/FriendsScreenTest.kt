package com.mclowicz.testfriends.friends

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.mclowicz.testfriends.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalAnimationApi
class FriendsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun showsEmptyFriendsMessage() {
        launchFriendsFor(
            email = "email@email.com",
            password = "passWORD123!@#",
            rule = composeTestRule
        ) {
            tapOnFriends()
        } verify {
            emptyFriendsMessageIsVisible()
        }
    }
}