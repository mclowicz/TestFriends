package com.mclowicz.testfriends.infrastructure

import com.mclowicz.testfriends.domain.user.User
import java.util.*

class UserBuilder {
    private var userId: String = UUID.randomUUID().toString()
    private var userEmail: String = "user@email.com"

    companion object {
        fun aUser(): UserBuilder {
            return UserBuilder()
        }
    }

    fun withId(id: String): UserBuilder = this.apply {
        userId = id
    }

    fun build(): User {
        return User(userId, userEmail)
    }
}