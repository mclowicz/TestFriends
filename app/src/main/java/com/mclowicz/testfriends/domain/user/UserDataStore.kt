package com.mclowicz.testfriends.domain.user


interface UserDataStore {

    fun loggedInUser(): String

    fun storeLoggedInUserId(userId: String)
}