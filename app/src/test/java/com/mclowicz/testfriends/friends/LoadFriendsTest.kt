package com.mclowicz.testfriends.friends

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.domain.user.*
import com.mclowicz.testfriends.infrastructure.UserBuilder.Companion.aUser
import com.mclowicz.testfriends.presentation.friends.FriendsState
import com.mclowicz.testfriends.presentation.friends.FriendsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class LoadFriendsTest {

    val user = aUser().withId("userId").build()
    val user2 = aUser().withId("xyzId").build()
    val user3 = aUser().withId("zyxId").build()
    val friend = Friend(user, isFollowee = false)
    val friend2 = Friend(user2, isFollowee = false)

    @Test
    fun friendsDoesNotExists() = runTest  {
        val userCatalog = InMemoryUserCatalog()
        val repository = FriendsRepository(userCatalog)
        val viewModel = FriendsViewModel(repository)
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.state.collectLatest {  }
        }

        viewModel.loadFriends(user.id)
        runCurrent()

        assertEquals(
            FriendsState(emptyList<Friend>()),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun loadedSingleFriend() = runTest {
        val userCatalog = InMemoryUserCatalog(
            usersForPassword = mutableMapOf(":irrelevant" to mutableListOf(user))
        )
        val repository = FriendsRepository(userCatalog)
        val viewModel = FriendsViewModel(repository)
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.state.collectLatest {  }
        }

        viewModel.loadFriends(user2.id)
        runCurrent()

        assertEquals(
            FriendsState(listOf(friend)),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun loadedMultipleFriends() = runTest {
        val userCatalog = InMemoryUserCatalog(
            usersForPassword = mutableMapOf(":irrelevant" to mutableListOf(user, user2))
        )
        val repository = FriendsRepository(userCatalog)
        val viewModel = FriendsViewModel(repository)
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.state.collectLatest {  }
        }

        viewModel.loadFriends(user3.id)
        runCurrent()

        assertEquals(
            FriendsState(listOf(friend, friend2)),
            viewModel.state.value
        )
        job.cancel()
    }
}