package com.mclowicz.testfriends.timeline

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.di.PostModule
import com.mclowicz.testfriends.domain.post.InMemoryPostCatalog
import com.mclowicz.testfriends.domain.post.Post
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
class TimeLineScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    val email = "user2@email.com"
    val password = "passWORD123!@#"
    val post1 = Post("post1", "user2Id", "This is first post.", 1L)
    val post2 = Post("post2", "user2Id", "The second post.", 2L)

    @Module
    @InstallIn(SingletonComponent::class)
    class TestModule {

        @Singleton
        @Provides
        fun providePostCatalog(): PostCatalog = InMemoryPostCatalog(
            mutableListOf(
                Post("post1", "user2Id", "This is first post.", 1L),
                Post("post2", "user2Id", "The second post.", 2L)
            )
        )
    }

    @Test
    fun showsEmptyTimelineMessage() {
        launchTimelineScreenFor("user1@user.com", password, composeTestRule) {
            // no actions
        } verify {
            emptyTimelineMessageIsVisible()
        }
    }

    @Test
    fun showAvailablePost() {
        launchTimelineScreenFor(email, password, composeTestRule) {
            // no actions
        } verify {
            postsAreVisible(post1, post2)
        }
    }

    @Test
    fun opensPostComposer() {
        launchTimelineScreenFor("test@email.com", "passWORD123!@#", composeTestRule) {
            tapOnCreateNewPost()
        } verify {
            newPostComposerIsDisplayed()
        }
    }

    @Test
    fun opensFriends() {
        launchTimelineScreenFor("test@email.com", "passWORD123!@#", composeTestRule) {
            tapOnFriends()
        } verify {
            friendsScreenIsPresent()
        }
    }
}