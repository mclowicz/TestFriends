package com.mclowicz.testfriends.friends

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.di.UserModule
import com.mclowicz.testfriends.domain.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@HiltAndroidTest
@ExperimentalAnimationApi
@UninstallModules(UserModule::class)
class FriendsScreenAvailableTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    companion object {
        val user1 = User("friend1Id", "friend1@email.com")
        val user2 = User("friend2Id", "friend2@email.com")
        val friend1 = Friend(user1, true)
        val friend2 = Friend(user2, false)
        val users = mutableMapOf("" to mutableListOf(user1, user2))
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class TestModule {

        @Provides
        @Singleton
        fun provideUserCatalog(): UserCatalog = InMemoryUserCatalog(
            usersForPassword = users,
            followings = mutableListOf(Following(userId = "email3Id", followedId = user2.id))
        )

        @Provides
        @Singleton
        fun provideInMemoryUserDataStore(): UserDataStore = InMemoryUserDataStore()
    }

    @Test
    fun showAvailableFriends() {
        launchFriendsFor(
            email = "email2@email.com",
            password = "passWORD123!@#",
            rule = composeTestRule
        ) {
            // no action
        } verify {
            friendsAreDisplayed(friend1, friend2)
        }
    }

    @Test
    fun followAFriend() {
        launchFriendsFor(
            email = "email@email.com",
            password = "passWORD123!@#",
            rule = composeTestRule
        ) {
            // no action
            tapOnFollowFor(friend1)
        } verify {
            followingIsAddedFor(friend1)
        }
    }

    @Test
    fun unfollowAFriend() {
        launchFriendsFor(
            email = "email3@email.com",
            password = "passWORD123!@#",
            rule = composeTestRule
        ) {
            // no action
            tapOnUnfollowFor(friend2)
        } verify {
            unfollowingIsAddedFor(friend2)
        }
    }
}