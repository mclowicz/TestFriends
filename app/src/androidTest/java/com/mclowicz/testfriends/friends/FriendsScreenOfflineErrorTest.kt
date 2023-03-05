package com.mclowicz.testfriends.friends

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.OfflineFriendsCatalog
import com.mclowicz.testfriends.di.UserModule
import com.mclowicz.testfriends.domain.user.InMemoryUserDataStore
import com.mclowicz.testfriends.domain.user.UserCatalog
import com.mclowicz.testfriends.domain.user.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(UserModule::class)
class FriendsScreenOfflineErrorTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Module
    @InstallIn(SingletonComponent::class)
    class TestModule {

        @Provides
        @Singleton
        fun provideUserCatalog(): UserCatalog = OfflineFriendsCatalog()

        @Provides
        @Singleton
        fun provideInMemoryUserData(): UserDataStore = InMemoryUserDataStore("emailId")
    }

    @Test
    fun showsOfflineError() {
        launchFriendsFor(
            email = "email@email.com",
            password = "passWORD123!@#",
            rule = composeTestRule
        ) {
            // no action
        } verify {
            offlineErrorIsDisplayed()
        }
    }
}