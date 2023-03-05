package com.mclowicz.testfriends.signup

import com.mclowicz.testfriends.InstantTaskExecutorExtension
import com.mclowicz.testfriends.OfflineUserCatalog
import com.mclowicz.testfriends.UnavailableUserCatalog
import com.mclowicz.testfriends.domain.user.InMemoryUserDataStore
import com.mclowicz.testfriends.domain.user.UserCatalog
import com.mclowicz.testfriends.domain.user.UserRepository
import com.mclowicz.testfriends.domain.validation.RegexCredentialsValidator
import com.mclowicz.testfriends.presentation.signup.Error
import com.mclowicz.testfriends.presentation.signup.SignUpViewModel
import com.mclowicz.testfriends.presentation.signup.SignupState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class FailedAccountCreationTest {

    private val regexCredentialsValidator = RegexCredentialsValidator()
    private val dataStore = InMemoryUserDataStore()
    private lateinit var viewModel: SignUpViewModel

    @Test
    fun backendError() = runTest {
        viewModel = replaceUserCatalogWith(UnavailableUserCatalog())
        val job = launch(NonCancellable) { viewModel.state.collectLatest {  } }

        with(viewModel) {
            setEmailField("email@email.com")
            setPasswordField("password123ASD.!")
            createAccount()
        }
        runCurrent()

        assertEquals(
            SignupState(error = Error.Backend),
            viewModel.state.value
        )
        job.cancel()
    }

    @Test
    fun offlineError() = runTest {
        viewModel = replaceUserCatalogWith(OfflineUserCatalog())
        val job = launch(NonCancellable) { viewModel.state.collectLatest {  } }

        with(viewModel) {
            setEmailField("email@email.com")
            setPasswordField("password123ASD.!")
            createAccount()
        }
        runCurrent()

        assertEquals(
            SignupState(error = Error.Offline),
            viewModel.state.value
        )
        job.cancel()
    }

    private fun replaceUserCatalogWith(userCatalog: UserCatalog): SignUpViewModel =
        SignUpViewModel(
            regexCredentialsValidator,
            UserRepository(userCatalog, dataStore)
        )
}