@file:OptIn(ExperimentalAnimationApi::class)

package com.mclowicz.testfriends.postComposer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.timeline.launchTimelineScreenFor

private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

@ExperimentalAnimationApi
fun launchPostComposerFor(
    email: String,
    composeTestRule: MainActivityRule,
    block: CreateNewPostRobot.() -> Unit
): CreateNewPostRobot {
    launchTimelineScreenFor(email, "passWORD123!@#" ,composeTestRule) {
        tapOnCreateNewPost()
    }
    return CreateNewPostRobot(composeTestRule).apply(block)
}

class CreateNewPostRobot(
    private val rule: MainActivityRule
    )
{

    fun typePost(postContent: String) {
        val newPostHint = rule.activity.getString(R.string.newPostHint)
        rule.onNodeWithText(newPostHint)
            .performTextInput(postContent)
    }

    fun submit() {
        val submitPost = rule.activity.getString(R.string.submitPost)
        rule.onNodeWithTag(submitPost)
            .performClick()
    }

    fun tapOnCreateNewPost() {
        val createNewPost = rule.activity.getString(R.string.createNewPost)
        rule.onNodeWithTag(createNewPost)
            .performClick()
    }

    infix fun verify(
        block: CreateNewPostVerificationRobot.() -> Unit
    ): CreateNewPostVerificationRobot {
        return CreateNewPostVerificationRobot(rule).apply(block)
    }
}

class CreateNewPostVerificationRobot(
    private val rule: MainActivityRule

) {
    fun newlyCreatedPostIsShown(
        userId: String,
        dateTime: String,
        postContent: String
    ) {
        rule.onAllNodesWithText(userId)
            .onFirst().assertIsDisplayed()
        rule.onAllNodesWithText(userId)
            .onLast().assertIsDisplayed()
        rule.onAllNodesWithText(dateTime)
            .onFirst().assertIsDisplayed()
        rule.onAllNodesWithText(dateTime)
            .onLast().assertIsDisplayed()
        rule.onNodeWithText(postContent)
            .assertIsDisplayed()
    }

    fun backendErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.creatingPostError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun offlineErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun loadingIsDisplayed() {
        val loadingTag = rule.activity.getString(R.string.loading)
        rule.onNodeWithTag(loadingTag)
            .assertIsDisplayed()
    }

}