package com.mclowicz.testfriends.domain.user

class InMemoryUserDataStore (
    private var loggedInUserId: String = String()
) : UserDataStore {

    override fun loggedInUser() = loggedInUserId
    override fun storeLoggedInUserId(userId: String) {
        loggedInUserId = userId
    }
}