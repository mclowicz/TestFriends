package com.mclowicz.testfriends.domain.user

interface UserCatalog {

    suspend fun createUser(
        email: String,
        password: String,
    ): User

    suspend fun followedBy(userId: String): List<String>

    suspend fun loadFriendsFor(userId: String): List<Friend>

    suspend fun toggleFollowing(userId: String, friendId: String): List<Friend>
}