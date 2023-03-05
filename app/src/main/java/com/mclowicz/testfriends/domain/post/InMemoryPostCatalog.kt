package com.mclowicz.testfriends.domain.post

import com.mclowicz.testfriends.infrastructure.Clock
import com.mclowicz.testfriends.infrastructure.IdGenerator
import com.mclowicz.testfriends.infrastructure.SystemClock
import com.mclowicz.testfriends.infrastructure.UUIDGenerator

class InMemoryPostCatalog(
    private val posts: MutableList<Post> = mutableListOf(),
    private val idGenerator: IdGenerator = UUIDGenerator(),
    private val clock: Clock = SystemClock()
) : PostCatalog {

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        return posts.filter { userIds.contains(it.userId) }
    }

    override suspend fun addPost(userId: String, postText: String): Post {
        val timeStamp = clock.now()
        val postId = idGenerator.next()
        val post = Post(postId, userId, postText, timeStamp)
        posts.add(post)
        return Post(
            postId,
            userId,
            postText,
            timeStamp
        )
    }
}