package com.mclowicz.testfriends.domain.post

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

abstract class PostCatalogContract {
    private val userId = "userId"
    private val userPost = Post("userPostId", userId, "New post", 1L)
    private val userPost2 = Post("user2PostId", "user2Id", "New post", 1L)
    private val userPost3 = Post("user3PostId", "user3Id", "New post", 1L)

    @Test
    fun postFound() = runTest {
        val postCatalog = postCatalogWith(userPost, userPost2, userPost3)

        val result = postCatalog.postsFor(listOf(userId))

        assertEquals(
            listOf(userPost),
            result
        )
    }

    @Test
    fun postNotFound() = runTest {
        val postCatalog = postCatalogWith(userPost2, userPost3)

        val result = postCatalog.postsFor(listOf(userId))

        assertEquals(
            listOf<Post>(),
            result
        )
    }

    @Test
    fun addNewPost() = runTest {
        val postCatalog = postCatalogWith(userPost2)
        val newlyAddedPost = postCatalog.addPost(userId, "New post")

        val result = postCatalog.postsFor(listOf(userId))

        assertEquals(
            listOf<Post>(newlyAddedPost),
            result
        )
    }

    protected abstract fun postCatalogWith(vararg posts: Post): PostCatalog
}