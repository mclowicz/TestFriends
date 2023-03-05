package com.mclowicz.testfriends.signup

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.R

@ExperimentalAnimationApi
fun launchSignUpScreen(
    rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: SignUpRobot.() -> Unit
): SignUpRobot = SignUpRobot(rule).apply(block)

@ExperimentalAnimationApi
class SignUpRobot(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    infix fun verify(block: SignUpVerification.() -> Unit): SignUpVerification {
        return SignUpVerification(rule).apply(block)
    }

    fun typeEmail(userEmail: String) {
        val emailTag = rule.activity.getString(R.string.email)
        rule.onNodeWithTag(emailTag)
            .performTextInput(userEmail)
    }

    fun typePassword(userPassword: String) {
        val passwordTag = rule.activity.getString(R.string.password)
        rule.onNodeWithTag(passwordTag)
            .performTextInput(userPassword)
    }

    fun submit() {
        val signupTag = rule.activity.getString(R.string.signup_screen_title)
        rule.onNodeWithTag(signupTag)
            .performClick()
    }

}

@ExperimentalAnimationApi
class SignUpVerification(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {
    fun timelineScreenIsPresent() {
        val timelineTitle = rule.activity.getString(R.string.timeline_screen_title)
        rule.onAllNodesWithText(timelineTitle)
            .onFirst()
            .assertIsDisplayed()
    }

    fun wrongEmailIsPresent() {
        val emailError = rule.activity.getString(R.string.emailError)
        rule.onNodeWithText(emailError)
            .assertIsDisplayed()
    }

    fun badEmailErrorIsNotShowing() {
        val emailError = rule.activity.getString(R.string.emailError)
        rule.onNodeWithText(emailError)
            .assertDoesNotExist()
    }

    fun wrongPasswordIsPresent() {
        val passwordError = rule.activity.getString(R.string.passwordError)
        rule.onNodeWithText(passwordError)
            .assertIsDisplayed()
    }

    fun badPasswordIsNotShowing() {
        val passwordError = rule.activity.getString(R.string.passwordError)
        rule.onNodeWithText(passwordError)
            .assertDoesNotExist()
    }

    fun duplicateAccountErrorIsVisible() {
        val duplicateAccountError = rule.activity.getString(R.string.duplicateAccountError)
        rule.onNodeWithText(duplicateAccountError)
            .assertExists()
    }

    fun offlineIsVisible() {
        val offlineError = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(offlineError)
            .assertExists(offlineError)
    }

    fun backendErrorIsVisible() {
        val backendError = rule.activity.getString(R.string.createAccountError)
        rule.onNodeWithText(backendError)
            .assertExists(backendError)
    }

    fun loadingIsShowing() {
        val loading = rule.activity.getString(R.string.loading)
        rule.onNodeWithTag(loading)
            .assertExists()
    }
}