package com.mclowicz.testfriends.signup

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.domain.user.InMemoryUserCatalog
import com.mclowicz.testfriends.domain.user.InMemoryUserDataStore
import com.mclowicz.testfriends.domain.user.User
import com.mclowicz.testfriends.domain.user.UserRepository
import com.mclowicz.testfriends.domain.validation.RegexCredentialsValidator
import com.mclowicz.testfriends.presentation.signup.SignUpEvent
import com.mclowicz.testfriends.presentation.signup.SignUpViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class CredentialsValidationTest {

    private val regexCredentialsValidator = RegexCredentialsValidator()
    private val dataStore = InMemoryUserDataStore()
    private val viewModel = SignUpViewModel(
        regexCredentialsValidator,
        UserRepository(InMemoryUserCatalog(), dataStore)
    )

    @ParameterizedTest
    @CsvSource(
        "'email'",
        "'a@b.c'",
        "'ab@b.c'",
        "'ab@bc.c'",
        "''",
        "'     '"
    )
    fun invalidEmail(email: String) {

        with(viewModel) {
            setEmailField(email)
            setPasswordField("password123ASD.!")
            createAccount()
        }

        Assertions.assertEquals(
            true,
            viewModel.state.value.isEmailError
        )
    }

    @ParameterizedTest
    @CsvSource(
        "'p'",
        "''",
        "'     '",
        "'12345678'",
        "'abc123456'",
        "'asds23#v6'",
        "'ABVGT567!'",
    )
    fun invalidPassword(password: String) {
        with(viewModel) {
            setEmailField("email@email.com")
            setPasswordField(password)
            createAccount()
        }

        Assertions.assertEquals(
            true,
            viewModel.state.value.isPasswordError
        )
    }

    @Test
    fun validCredentials() = runTest {
        val user = User(
            id = "userId",
            email = "email@email.com",
        )
        val job = launch(NonCancellable) { viewModel.event.collectLatest {  } }

        with(viewModel) {
            setEmailField(user.email)
            setPasswordField("12QRandk3!..")
            createAccount()
        }
        runCurrent()

        Assertions.assertEquals(
            SignUpEvent.SignedUp(user),
            viewModel.event.value
        )
        job.cancel()
    }
}