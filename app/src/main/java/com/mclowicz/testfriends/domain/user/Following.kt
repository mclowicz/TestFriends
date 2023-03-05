package com.mclowicz.testfriends.domain.user

data class Following(
    val userId: String,
    val followedId: String
)
