package com.mclowicz.testfriends.timeline

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.UnavailablePostCatalog
import com.mclowicz.testfriends.di.PostModule
import com.mclowicz.testfriends.domain.post.PostCatalog
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
@UninstallModules(PostModule::class)
class TimelineScreenBackendTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Module
    @InstallIn(SingletonComponent::class)
    class TestModule {

        @Singleton
        @Provides
        fun providePostCatalog(): PostCatalog = UnavailablePostCatalog()
    }

    @Test
    fun showsBackendError() {
        launchTimelineScreenFor("test2@email.com", "passWORD123!@#", composeTestRule) {
            // no actions
        } verify {
            backendErrorIsDisplayed()
        }
    }
}