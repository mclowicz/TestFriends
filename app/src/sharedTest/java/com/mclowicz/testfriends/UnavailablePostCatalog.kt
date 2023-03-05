package com.mclowicz.testfriends

import com.mclowicz.testfriends.domain.exceptions.BackendException
import com.mclowicz.testfriends.domain.post.Post
import com.mclowicz.testfriends.domain.post.PostCatalog

class UnavailablePostCatalog : PostCatalog {

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        throw BackendException()
    }

    override suspend fun addPost(userId: String, postText: String): Post {
        throw BackendException()
    }
}