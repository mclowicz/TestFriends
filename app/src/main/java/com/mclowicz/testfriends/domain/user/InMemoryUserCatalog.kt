package com.mclowicz.testfriends.domain.user

import com.mclowicz.testfriends.domain.exceptions.DuplicateAccountException

class InMemoryUserCatalog(
    private val usersForPassword: MutableMap<String, MutableList<User>> = mutableMapOf(),
    private val followings: MutableList<Following> = mutableListOf()
) : UserCatalog {

    private fun checkAccountExists(email: String) {
        if (usersForPassword.values.flatten().any { it.email == email }) {
            throw DuplicateAccountException()
        }
    }

    private fun createUserIdFor(email: String): String = email.takeWhile { it != '@' } + "Id"

    private fun saveUser(password: String, user: User) {
        usersForPassword.getOrPut(password, ::mutableListOf).add(user)
    }

    override suspend fun createUser(email: String, password: String): User {
        checkAccountExists(email)
        val userId = createUserIdFor(email)
        val user = User(userId, email)
        saveUser(password, user)
        return user
    }

    override suspend fun followedBy(userId: String): List<String> {
        return followings
            .filter { it.userId == userId }
            .map { it.followedId }
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        val friendsFollowedByUser = followedBy(userId)
        val allUsers = usersForPassword.values.flatten()
        return allUsers
            .filter { it.id != userId }
            .map { user -> Friend(user, user.id in friendsFollowedByUser) }
    }

    override suspend fun toggleFollowing(userId: String, friendId: String): List<Friend> {
        val following = Following(userId, friendId)
        if (followings.contains(following)) {
            followings.remove(following)
        } else {
            followings.add(following)
        }
        return loadFriendsFor(userId)
    }
}