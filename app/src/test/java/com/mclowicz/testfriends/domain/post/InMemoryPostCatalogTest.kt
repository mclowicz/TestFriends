package com.mclowicz.testfriends.domain.post

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class InMemoryPostCatalogTest : PostCatalogContract() {

    override fun postCatalogWith(vararg posts: Post): PostCatalog =
        InMemoryPostCatalog(
            posts = posts.toMutableList()
        )
}