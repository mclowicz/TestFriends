package com.mclowicz.testfriends.presentation.friends

import androidx.annotation.StringRes
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.domain.user.Friend

data class FriendsState(
    val friends: List<Friend>? = null,
    val isLoading: Boolean = false,
    val error: Error? = null
)

sealed class Error(@StringRes val msgResId: Int) {
    object Offline : Error(msgResId = R.string.offlineError)
    object Backend : Error(msgResId = R.string.friends_error)
}