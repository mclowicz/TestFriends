package com.mclowicz.testfriends.domain.timeline

import com.mclowicz.testfriends.domain.exceptions.BackendException
import com.mclowicz.testfriends.domain.exceptions.OfflineException
import com.mclowicz.testfriends.domain.post.PostCatalog
import com.mclowicz.testfriends.domain.user.UserCatalog
import com.mclowicz.testfriends.presentation.timeline.Error
import com.mclowicz.testfriends.presentation.timeline.TimelineState
import javax.inject.Inject

class TimeLineRepository @Inject constructor(
    val userCatalog: UserCatalog,
    val postCatalog: PostCatalog
) {

    suspend fun getTimelineFor(userId: String): TimelineState {
        return try {
            val userIds = listOf(userId) + userCatalog.followedBy(userId)
            val postsForUser = postCatalog.postsFor(userIds)
            TimelineState(posts = postsForUser)
        } catch (e: BackendException) {
            TimelineState(error = Error.Backend)
        } catch (e: OfflineException) {
            TimelineState(error = Error.Offline)
        }
    }
}