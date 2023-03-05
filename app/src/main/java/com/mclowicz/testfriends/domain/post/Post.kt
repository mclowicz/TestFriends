package com.mclowicz.testfriends.domain.post

data class Post(
    val id: String,
    val userId: String,
    val postText: String,
    val timeStamp: Long
)
