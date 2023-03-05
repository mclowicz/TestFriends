package com.mclowicz.testfriends.postComposer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.OfflinePostCatalog
import com.mclowicz.testfriends.di.PostModule
import com.mclowicz.testfriends.di.UserModule
import com.mclowicz.testfriends.domain.post.PostCatalog
import com.mclowicz.testfriends.domain.user.InMemoryUserCatalog
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
@UninstallModules(UserModule::class, PostModule::class)
class PostComposerOfflineErrorTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Module
    @InstallIn(SingletonComponent::class)
    class TestModule {

        @Provides
        @Singleton
        fun provideUserCatalog(): UserCatalog = InMemoryUserCatalog()

        @Provides
        @Singleton
        fun provideInMemoryUserData(): UserDataStore = InMemoryUserDataStore("emailId")
    }

    @Module
    @InstallIn(SingletonComponent::class)
    class TestPostModule {

        @Provides
        @Singleton
        fun providePostCatalog(): PostCatalog = OfflinePostCatalog()
    }

    @Test
    fun showsOfflineError() {
        launchPostComposerFor("email@email.com", composeTestRule) {
            typePost("My new post")
            submit()
        } verify {
            offlineErrorIsDisplayed()
        }
    }
}