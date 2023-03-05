package com.mclowicz.testfriends.domain.post

import com.mclowicz.testfriends.domain.exceptions.BackendException
import com.mclowicz.testfriends.domain.exceptions.OfflineException
import com.mclowicz.testfriends.domain.user.UserDataStore
import com.mclowicz.testfriends.presentation.postComposer.CreatePostState
import com.mclowicz.testfriends.presentation.postComposer.Error
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val userDataStore: UserDataStore,
    private val postCatalog: PostCatalog
) {

    suspend fun createNewPost(postText: String): CreatePostState {
        return try {
            val post = postCatalog.addPost(userDataStore.loggedInUser(), postText)
            CreatePostState(post = post)
        } catch (e: BackendException) {
            CreatePostState(error = Error.Backend)
        } catch (e: OfflineException) {
            CreatePostState(error = Error.Offline)
        }
    }
}