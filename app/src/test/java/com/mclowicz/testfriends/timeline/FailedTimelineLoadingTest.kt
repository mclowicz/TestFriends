package com.mclowicz.testfriends.timeline

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.OfflinePostCatalog
import com.mclowicz.testfriends.UnavailablePostCatalog
import com.mclowicz.testfriends.domain.timeline.TimeLineRepository
import com.mclowicz.testfriends.domain.user.InMemoryUserCatalog
import com.mclowicz.testfriends.presentation.timeline.Error
import com.mclowicz.testfriends.presentation.timeline.TimelineState
import com.mclowicz.testfriends.presentation.timeline.TimelineViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class FailedTimelineLoadingTest {

    @Test
    fun backendError() = runTest {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = UnavailablePostCatalog()
        val timelineRepository = TimeLineRepository(
            userCatalog,
            postCatalog
        )
        val viewModel = TimelineViewModel(timelineRepository)

        val job = launch(NonCancellable) {
            viewModel.state.collectLatest {  }
        }

        viewModel.timelineFor("userId")
        runCurrent()

        Assertions.assertEquals(
            TimelineState(userId = "userId", error = Error.Backend),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun offlineError() = runTest {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = OfflinePostCatalog()
        val timelineRepository = TimeLineRepository(
            userCatalog,
            postCatalog
        )
        val viewModel = TimelineViewModel(timelineRepository)

        val job = launch(NonCancellable) {
            viewModel.state.collectLatest {  }
        }

        viewModel.timelineFor("userId")
        runCurrent()

        Assertions.assertEquals(
            TimelineState(userId = "userId", error = Error.Offline),
            viewModel.state.value
        )
        job.cancel()
    }
}