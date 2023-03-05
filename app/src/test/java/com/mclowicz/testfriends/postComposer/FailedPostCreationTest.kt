package com.mclowicz.testfriends.postComposer

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.OfflinePostCatalog
import com.mclowicz.testfriends.domain.post.PostRepository
import com.mclowicz.testfriends.domain.user.InMemoryUserDataStore
import com.mclowicz.testfriends.presentation.postComposer.CreatePostState
import com.mclowicz.testfriends.presentation.postComposer.CreatePostViewModel
import com.mclowicz.testfriends.presentation.postComposer.Error
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.mclowicz.testfriends.UnavailablePostCatalog as UnavailablePostCatalog1

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class FailedPostCreationTest {

    @Test
    fun backendError() = runTest{
        val userData = InMemoryUserDataStore("userId")
        val postCatalog = UnavailablePostCatalog1()
        val viewModel = CreatePostViewModel(
            PostRepository(
                userData,
                postCatalog
            )
        )
        val job = launch(NonCancellable) {
            viewModel.state.collectLatest {  }
        }

        viewModel.createPost(":backend:")
        runCurrent()

        assertEquals(
            CreatePostState(error = Error.Backend),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun offlineError() = runTest {
        val userData = InMemoryUserDataStore("userId")
        val postCatalog = OfflinePostCatalog()
        val viewModel = CreatePostViewModel(
            PostRepository(userData, postCatalog)
        )
        val job = launch(NonCancellable) {
            viewModel.state.collectLatest {  }
        }

        viewModel.createPost(":offline:")
        runCurrent()

        assertEquals(
            CreatePostState(error = Error.Offline),
            viewModel.state.value
        )
        job.cancel()
    }
}