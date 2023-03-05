package com.mclowicz.testfriends.presentation.postComposer

import androidx.annotation.StringRes
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.domain.post.Post

data class CreatePostState(
    val post: Post? = null,
    val isLoading: Boolean = false,
    val error: Error? = null
)

sealed class Error(@StringRes val msgResId: Int) {
    object Offline : Error(msgResId = R.string.offlineError)
    object Backend : Error(msgResId = R.string.creatingPostError)
}