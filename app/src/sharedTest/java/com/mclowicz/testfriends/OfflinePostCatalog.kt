package com.mclowicz.testfriends

import com.mclowicz.testfriends.domain.exceptions.OfflineException
import com.mclowicz.testfriends.domain.post.Post
import com.mclowicz.testfriends.domain.post.PostCatalog

class OfflinePostCatalog : PostCatalog {
    override suspend fun postsFor(userIds: List<String>): List<Post> {
        throw OfflineException()
    }

    override suspend fun addPost(userId: String, postText: String): Post {
        throw OfflineException()
    }
}