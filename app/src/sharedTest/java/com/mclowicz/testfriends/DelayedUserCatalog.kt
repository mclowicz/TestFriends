package com.mclowicz.testfriends

import com.mclowicz.testfriends.domain.user.Friend
import com.mclowicz.testfriends.domain.user.User
import com.mclowicz.testfriends.domain.user.UserCatalog
import kotlinx.coroutines.delay

class DelayedUserCatalog : UserCatalog {
    override suspend fun createUser(email: String, password: String): User {
        delay(2000)
        return User("emailId", "email@firends.app")
    }

    override suspend fun followedBy(userId: String): List<String> {
        delay(2000)
        return emptyList()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        delay(2000)
        return emptyList()
    }

    override suspend fun toggleFollowing(userId: String, friendId: String): List<Friend> {
        delay(2000)
        val user = User("friend1Id", "friend1@email.com")
        return listOf(Friend(user = user, isFollowee = false))
    }
}