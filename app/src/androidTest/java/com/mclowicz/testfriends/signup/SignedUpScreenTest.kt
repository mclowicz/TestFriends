package com.mclowicz.testfriends.signup

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.di.UserModule
import com.mclowicz.testfriends.domain.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import javax.inject.Singleton

@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(UserModule::class)
class SignedUpScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Module
    @InstallIn(SingletonComponent::class)
    class TestModule {

        @Singleton
        @Provides
        fun provideDuplicateAccountBackend(): UserCatalog = InMemoryUserCatalog(
            usersForPassword = mutableMapOf(
                "passWORD123!@#" to mutableListOf(User("emailId", "email@firends.app"))
            )
        )

        @Provides
        @Singleton
        fun provideInMemoryUserData(): UserDataStore = InMemoryUserDataStore("emailId")
    }

    private val userEmail = "user@firends.app"
    private val user2Email = "email@firends.app"
    private val userPassword = "password!23.GJ"

    @Test
    fun performSignUp() {
        launchSignUpScreen(composeTestRule) {
            typeEmail(userEmail)
            typePassword(userPassword)
            submit()
        } verify {
            timelineScreenIsPresent()
        }
    }

    @Test
    fun displayEmailError() {
        launchSignUpScreen(composeTestRule) {
            typeEmail("badEmail")
            typePassword(userPassword)
        } verify {
            wrongEmailIsPresent()
        }
    }

    @Test
    fun resetEmailError() {
        launchSignUpScreen(composeTestRule) {
            typeEmail("badEmail")
            submit()
            typeEmail("badEmail@email.com")
        } verify {
            badEmailErrorIsNotShowing()
        }
    }

    @Test
    fun displayPasswordError() {
        launchSignUpScreen(composeTestRule) {
            typeEmail(userEmail)
            typePassword("123")
            submit()
        } verify {
            wrongPasswordIsPresent()
        }
    }

    @Test
    fun resetPasswordError() {
        launchSignUpScreen(composeTestRule) {
            typePassword("badPassword")
            submit()
            typePassword("passWORD123!@#")
        } verify {
            badPasswordIsNotShowing()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun displayDuplicateAccountError() {
        launchSignUpScreen(composeTestRule) {
            typeEmail(user2Email)
            typePassword(userPassword)
            submit()
        } verify {
            duplicateAccountErrorIsVisible()
        }
    }
}