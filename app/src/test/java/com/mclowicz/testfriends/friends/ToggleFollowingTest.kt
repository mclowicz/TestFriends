package com.mclowicz.testfriends.friends

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.domain.user.Following
import com.mclowicz.testfriends.domain.user.Friend
import com.mclowicz.testfriends.domain.user.FriendsRepository
import com.mclowicz.testfriends.domain.user.InMemoryUserCatalog
import com.mclowicz.testfriends.infrastructure.UserBuilder.Companion.aUser
import com.mclowicz.testfriends.presentation.friends.FriendsState
import com.mclowicz.testfriends.presentation.friends.FriendsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class ToggleFollowingTest {

    val user = aUser().withId("userId").build()
    val user2 = aUser().withId("xyzId").build()
    val user3 = aUser().withId("xyzId").build()
    val friend = Friend(user2, isFollowee = true)
    val friend2 = Friend(user3, isFollowee = false)

    @Test
    fun follow() = runTest {
        val userCatalog = InMemoryUserCatalog(
            usersForPassword = mutableMapOf(":irrelevant" to mutableListOf(user2))
        )
        val repository = FriendsRepository(userCatalog)
        val viewModel = FriendsViewModel(repository).apply {
            loadFriends(user.id)
        }
        val job = launch(NonCancellable) {
            viewModel.state.collectLatest {  }
        }

        viewModel.toggleFollowing(user.id, user2.id)
        runCurrent()

        Assertions.assertEquals(
            FriendsState(listOf(friend)),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun unFollow() = runTest {
        val userCatalog = InMemoryUserCatalog(
            usersForPassword = mutableMapOf(":irrelevant" to mutableListOf(user3)),
            followings = mutableListOf(
                Following(
                    userId = user.id,
                    followedId = friend2.user.id
                )
            )
        )
        val repository = FriendsRepository(userCatalog)
        val viewModel = FriendsViewModel(repository).apply {
            loadFriends(user.id)
        }
        val job = launch(NonCancellable) {
            viewModel.state.collectLatest {  }
        }

        viewModel.toggleFollowing(user.id, user3.id)
        runCurrent()

        Assertions.assertEquals(
            FriendsState(listOf(friend2)),
            viewModel.state.value
        )
        job.cancel()
    }
}