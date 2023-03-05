package com.mclowicz.testfriends.presentation.postComposer

import com.mclowicz.testfriends.domain.post.Post

sealed class CreatePostEvent {
    class Created(val post: Post) : CreatePostEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Created
            if (post != other.post) return false
            return true
        }

        override fun hashCode(): Int {
            return post.hashCode()
        }
    }
}