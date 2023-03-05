package com.mclowicz.testfriends.presentation.timeline

import androidx.annotation.StringRes
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.domain.post.Post

data class TimelineState(
    val userId: String = String(),
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: Error? = null
)

sealed class Error(@StringRes val msgResId: Int) {
    object Offline : Error(msgResId = R.string.offlineError)
    object Backend : Error(msgResId = R.string.fetchingTimelineError)
}