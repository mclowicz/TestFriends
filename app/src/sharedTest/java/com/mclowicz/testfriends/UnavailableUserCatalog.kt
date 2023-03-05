package com.mclowicz.testfriends

import com.mclowicz.testfriends.domain.exceptions.BackendException
import com.mclowicz.testfriends.domain.user.Friend
import com.mclowicz.testfriends.domain.user.User
import com.mclowicz.testfriends.domain.user.UserCatalog

class UnavailableUserCatalog() : UserCatalog {
    override suspend fun createUser(email: String, password: String): User {
        throw BackendException()
    }

    override suspend fun followedBy(userId: String): List<String> {
        throw BackendException()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        throw BackendException()
    }

    override suspend fun toggleFollowing(userId: String, friendId: String): List<Friend> {
        throw BackendException()
    }
}