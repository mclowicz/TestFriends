package com.mclowicz.testfriends.timeline

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.domain.post.InMemoryPostCatalog
import com.mclowicz.testfriends.domain.post.Post
import com.mclowicz.testfriends.domain.timeline.TimeLineRepository
import com.mclowicz.testfriends.domain.user.Following
import com.mclowicz.testfriends.domain.user.InMemoryUserCatalog
import com.mclowicz.testfriends.domain.user.User
import com.mclowicz.testfriends.infrastructure.UserBuilder.Companion.aUser
import com.mclowicz.testfriends.presentation.timeline.TimelineState
import com.mclowicz.testfriends.presentation.timeline.TimelineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class LoadTimelineTest {

    private val user = aUser().withId("userId").build()
    private val posts = mutableListOf(
        Post("1", "userId", "post text", 1L),
        Post("2", "zzzId", "post text", 1L),
        Post("3", "zzzId", "post text", 1L),
    )
    private val userCatalog = InMemoryUserCatalog(
        followings = mutableListOf(
            Following(user.id, "zzzId")
        )
    )
    private val postCatalog = InMemoryPostCatalog(
        posts = posts
    )
    private val timelineRepository = TimeLineRepository(
        userCatalog,
        postCatalog
    )
    private val viewModel = TimelineViewModel(timelineRepository)

    @Test
    fun noPostsAvailable() = runTest {
        val user = User("xyzId", "user@email.com")
        val job = launch(NonCancellable) { viewModel.state.collectLatest {  } }

        viewModel.timelineFor(user.id)
        runCurrent()

        assertEquals(
            TimelineState(userId = user.id, posts = emptyList()),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun postsAvailable() = runTest {
        val job = launch(NonCancellable) { viewModel.state.collectLatest {  } }

        viewModel.timelineFor(user.id)
        runCurrent()

        assertEquals(
            TimelineState(userId = user.id, posts = posts),
            viewModel.state.value
        )
        job.cancel()
    }
}