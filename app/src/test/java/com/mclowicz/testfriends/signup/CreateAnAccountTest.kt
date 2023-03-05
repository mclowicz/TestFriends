package com.mclowicz.testfriends.signup

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.domain.user.InMemoryUserCatalog
import com.mclowicz.testfriends.domain.user.InMemoryUserDataStore
import com.mclowicz.testfriends.domain.user.User
import com.mclowicz.testfriends.domain.user.UserRepository
import com.mclowicz.testfriends.domain.validation.RegexCredentialsValidator
import com.mclowicz.testfriends.presentation.signup.Error
import com.mclowicz.testfriends.presentation.signup.SignUpEvent
import com.mclowicz.testfriends.presentation.signup.SignUpViewModel
import com.mclowicz.testfriends.presentation.signup.SignupState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

    private val regexCredentialsValidator = RegexCredentialsValidator()
    private val dataStore = InMemoryUserDataStore()
    private val viewModel = SignUpViewModel(
        regexCredentialsValidator,
        UserRepository(InMemoryUserCatalog(), dataStore)
    )

    @Test
    fun accountCreated() = runTest {
        val user = User(
            "userId",
            "user@email.com",
        )
        val job = launch(NonCancellable) { viewModel.event.collectLatest { } }

        with(viewModel) {
            setEmailField(user.email)
            setPasswordField("password123ASD.!")
            createAccount()
        }
        runCurrent()

        assertEquals(
            SignUpEvent.SignedUp(user),
            viewModel.event.value
        )
        job.cancel()
    }

    @Test
    fun createDuplicateAccount() = runTest {
        val user = User(
            "user3Id",
            "user3@email.com",
        )
        val password = "!@123ABhyodds34"
        val userForPassword = mutableMapOf(password to mutableListOf(user))
        val userRepository = UserRepository(InMemoryUserCatalog(userForPassword), dataStore)
        val viewModel = SignUpViewModel(regexCredentialsValidator, userRepository)
        val job = launch(NonCancellable) { viewModel.state.collectLatest { } }

        with(viewModel) {
            setEmailField(user.email)
            setPasswordField(password)
            createAccount()
        }
        runCurrent()

        assertEquals(
            SignupState(error = Error.DuplicateAccount),
            viewModel.state.value
        )
        job.cancel()
    }
}