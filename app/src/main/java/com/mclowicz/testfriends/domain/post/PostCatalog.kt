package com.mclowicz.testfriends.domain.post

interface PostCatalog {

    suspend fun postsFor(userIds: List<String>): List<Post>

    suspend fun addPost(userId: String, postText: String): Post
}