package com.mclowicz.testfriends.domain.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class StoreSignedUpUserDataTest {

    @Test
    fun successSignUp() = runTest {
        val userDataStore = InMemoryUserDataStore()
        val userRepository = UserRepository(InMemoryUserCatalog(), userDataStore)

        userRepository.signUp("user@email.com", "password")

        assertEquals(
            "userId",
            userDataStore.loggedInUser()
        )
    }
}