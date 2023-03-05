package com.mclowicz.testfriends.friends

import com.mclowicz.testfriends.*
import com.mclowicz.testfriends.domain.user.*
import com.mclowicz.testfriends.presentation.friends.Error
import com.mclowicz.testfriends.presentation.friends.FriendsState
import com.mclowicz.testfriends.presentation.friends.FriendsViewModel
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
class FailFriendsLoadingTest {

    @Test
    fun backendError() = runTest {
        val viewModel = FriendsViewModel(FriendsRepository(UnavailableUserCatalog()))
        val job = launch(NonCancellable) {
            viewModel.state.collectLatest {  }
        }

        viewModel.loadFriends("zzzId")
        runCurrent()

        assertEquals(
            FriendsState(error = Error.Backend),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun offlineError() = runTest {
        val viewModel = FriendsViewModel(FriendsRepository(OfflineUserCatalog()))
        val job = launch(NonCancellable) {
            viewModel.state.collectLatest {  }
        }

        viewModel.loadFriends("zzzId")
        runCurrent()

        assertEquals(
            FriendsState(error = Error.Offline),
            viewModel.state.value
        )
        job.cancel()
    }
}