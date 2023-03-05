package com.mclowicz.testfriends

import com.mclowicz.testfriends.domain.exceptions.OfflineException
import com.mclowicz.testfriends.domain.user.Friend
import com.mclowicz.testfriends.domain.user.User
import com.mclowicz.testfriends.domain.user.UserCatalog

class OfflineFriendsCatalog : UserCatalog {

    override suspend fun createUser(email: String, password: String): User {
        return User("userId", "user@email.com")
    }

    override suspend fun followedBy(userId: String): List<String> {
        throw OfflineException()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        throw OfflineException()
    }

    override suspend fun toggleFollowing(userId: String, friendId: String): List<Friend> {
        throw OfflineException()
    }
}