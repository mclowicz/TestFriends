package com.mclowicz.testfriends.postComposer

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.domain.post.InMemoryPostCatalog
import com.mclowicz.testfriends.domain.post.Post
import com.mclowicz.testfriends.domain.post.PostRepository
import com.mclowicz.testfriends.domain.user.InMemoryUserDataStore
import com.mclowicz.testfriends.infrastructure.ControllableClock
import com.mclowicz.testfriends.infrastructure.ControllableIdGenerator
import com.mclowicz.testfriends.presentation.postComposer.CreatePostEvent
import com.mclowicz.testfriends.presentation.postComposer.CreatePostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAPostTest {

    private val idGenerator = ControllableIdGenerator("postId")
    private val clock = ControllableClock(1L)
    private val userId = "userId"
    private val userData = InMemoryUserDataStore(userId)
    private val postCatalog = InMemoryPostCatalog(
        idGenerator = idGenerator,
        clock = clock
    )

    @Test
    fun aPostIsCreated() = runTest {
        val postText = "First Post"
        val post = Post(
            "postId",
            "userId",
            postText,
            1L
        )
        val viewModel = CreatePostViewModel(
            PostRepository(userData, postCatalog)
        )
        val job = launch(NonCancellable) {
            viewModel.event.collectLatest {  }

            viewModel.createPost(postText)

            assertEquals(
                CreatePostEvent.Created(post),
                viewModel.event.value
            )
        }
        runCurrent()


        job.cancel()
    }

    @Test
    fun anotherPostCreated() = runTest {
        val postText = "Second Post"
        val anotherPost = Post(
            "postId",
            "userId",
            postText,
            1L
        )
        val viewModel = CreatePostViewModel(
            PostRepository(userData, postCatalog)
        )
        val job = launch(NonCancellable) {
            viewModel.event.collectLatest {  }
            viewModel.createPost(postText)
            assertEquals(
                CreatePostEvent.Created(anotherPost),
                viewModel.event.value
            )
        }
        runCurrent()
        job.cancel()
    }
}