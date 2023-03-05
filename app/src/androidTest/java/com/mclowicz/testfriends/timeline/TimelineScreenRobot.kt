package com.mclowicz.testfriends.timeline

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.domain.post.Post
import com.mclowicz.testfriends.signup.launchSignUpScreen

@ExperimentalAnimationApi
fun launchTimelineScreenFor(
    email: String,
    password: String,
    rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    block: TimelineRobot.() -> Unit
): TimelineRobot {
    launchSignUpScreen(rule) {
        typeEmail(email)
        typePassword(password)
        submit()
    }
    return TimelineRobot(rule).apply(block)
}

@ExperimentalAnimationApi
class TimelineRobot(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    infix fun verify(block: TimelineVerification.() -> Unit): TimelineVerification {
        return TimelineVerification(rule).apply(block)
    }

    fun tapOnCreateNewPost() {
        val createNewPost = rule.activity.getString(R.string.createNewPost)
        rule.onNodeWithTag(createNewPost)
            .performClick()

    }

    fun tapOnFriends() {
        val friends = rule.activity.getString(R.string.friends)
        rule.onNodeWithText(friends)
            .performClick()
    }
}


@ExperimentalAnimationApi
class TimelineVerification(
    private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    fun emptyTimelineMessageIsVisible() {
        val emptyTimelineMessage = rule.activity.getString(R.string.emptyTimelineMessage)
        rule.onNodeWithText(emptyTimelineMessage)
            .assertIsDisplayed()
    }

    fun postsAreVisible(vararg posts: Post) {
        posts.forEach { post ->
            rule.onNodeWithText(post.postText)
                .assertIsDisplayed()
        }
    }

    fun newPostComposerIsDisplayed() {
        val createNewPost = rule.activity.getString(R.string.createNewPost)
        rule.onNodeWithText(createNewPost)
            .assertIsDisplayed()
    }

    fun loadingIndicatorIsDisplayed() {
        val loading = rule.activity.getString(R.string.loading)
        rule.onNodeWithTag(loading)
            .assertIsDisplayed()
    }

    fun backendErrorIsDisplayed() {
        val backendError = rule.activity.getString(R.string.fetchingTimelineError)
        rule.onNodeWithText(backendError)
            .assertIsDisplayed()
    }

    fun offlineErrorIsDisplayed() {
        val offlineError = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(offlineError)
            .assertIsDisplayed()
    }

    fun friendsScreenIsPresent() {
        val friends = rule.activity.getString(R.string.friends)
        rule.onAllNodesWithText(friends)
            .onFirst()
            .assertIsDisplayed()
    }

}