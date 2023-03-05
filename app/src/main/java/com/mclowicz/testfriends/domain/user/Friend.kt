package com.mclowicz.testfriends.domain.user

data class Friend(
    val user: User,
    val isFollowee: Boolean
)