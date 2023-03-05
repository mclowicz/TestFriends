package com.mclowicz.testfriends.friends

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.mclowicz.testfriends.MainActivity
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.domain.user.Friend
import com.mclowicz.testfriends.timeline.launchTimelineScreenFor

@OptIn(ExperimentalAnimationApi::class)
private typealias MainActivityRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>


@ExperimentalAnimationApi
fun launchFriendsFor(
    email: String,
    password: String,
    rule: MainActivityRule,
    block: FriendsRobot.() -> Unit
) : FriendsRobot {
    launchTimelineScreenFor(email, password, rule) {}
    return FriendsRobot(rule).apply {
        tapOnFriends()
        block()
    }
}

@ExperimentalAnimationApi
class FriendsRobot(
    private val rule: MainActivityRule
    ) {

    fun tapOnFriends() {
        val people = rule.activity.getString(R.string.friends)
        rule.onAllNodesWithText(people)
            .onLast()
            .performClick()
    }

    infix fun verify(
        block: FriendsVerificationRobot.() -> Unit
    ): FriendsVerificationRobot {
        return FriendsVerificationRobot(rule).apply(block)
    }

    fun tapOnFollowFor(friend: Friend) {
        val followFriend = rule.activity.getString(R.string.followFriend, friend.user.id)
        rule.onNodeWithContentDescription(followFriend)
            .performClick()
    }

    fun tapOnUnfollowFor(friend: Friend) {
        val unfollowFriend = rule.activity.getString(R.string.unfollowFriend, friend.user.id)
        rule.onNodeWithContentDescription(unfollowFriend)
            .performClick()
    }
}

@ExperimentalAnimationApi
class FriendsVerificationRobot(
    private val rule: MainActivityRule
) {
    fun emptyFriendsMessageIsVisible() {
        val emptyFriendsMsg = rule.activity.getString(R.string.emptyFriendsMessage)
        rule.onNodeWithText(emptyFriendsMsg)
            .assertIsDisplayed()
    }

    fun friendsAreDisplayed(vararg friends: Friend) {
        friends.forEach { friend ->
            rule.onNodeWithText(friend.user.id)
                .assertIsDisplayed()
            rule.onNodeWithText(friend.user.email)
                .assertIsDisplayed()
        }
    }

    fun backendErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.friends_error)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun offlineErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun followingIsAddedFor(friend: Friend) {
        val unfollow = rule.activity.getString(R.string.unfollow, friend.user.id)
        val unfollowFriend = rule.activity.getString(R.string.unfollowFriend, friend.user.id)
        rule.onNode(hasText(unfollow).and(hasContentDescription(unfollowFriend)))
            .assertIsDisplayed()
    }

    fun unfollowingIsAddedFor(friend: Friend) {
        val follow = rule.activity.getString(R.string.follow, friend.user.id)
        val followFriend = rule.activity.getString(R.string.followFriend, friend.user.id)
        rule.onNode(hasText(follow).and(hasContentDescription(followFriend)))
            .assertIsDisplayed()
    }
}

