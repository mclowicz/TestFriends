package com.mclowicz.testfriends

import com.mclowicz.testfriends.domain.post.Post
import com.mclowicz.testfriends.domain.post.PostCatalog
import kotlinx.coroutines.delay

class DelayedPostCatalog : PostCatalog {
    override suspend fun postsFor(userIds: List<String>): List<Post> {
        delay(2000)
        return emptyList()
    }

    override suspend fun addPost(userId: String, postText: String): Post {
        delay(2000)
        return Post("postId", userId, postText, 0L)
    }

}