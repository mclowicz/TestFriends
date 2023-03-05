package com.mclowicz.testfriends.friends

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.domain.user.InMemoryUserCatalog
import com.mclowicz.testfriends.domain.user.FriendsRepository
import com.mclowicz.testfriends.presentation.friends.FriendsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingFriendsStateTest {

    @Test
    fun friendsStatesDeliveredInParticularOrder() = runTest {
        val states = mutableListOf<Boolean>()
        val repository = FriendsRepository(InMemoryUserCatalog())
        val viewModel = FriendsViewModel(repository)
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.state
                .onEach { states.add(it.isLoading) }
                .collectLatest {  }
        }

        viewModel.loadFriends("userId")
        runCurrent()

        assertEquals(
            listOf(false, true, false),
            states
        )
        job.cancel()
    }
}