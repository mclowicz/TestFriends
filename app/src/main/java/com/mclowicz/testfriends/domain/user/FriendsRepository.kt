package com.mclowicz.testfriends.domain.user

import com.mclowicz.testfriends.domain.exceptions.BackendException
import com.mclowicz.testfriends.domain.exceptions.OfflineException
import com.mclowicz.testfriends.presentation.friends.Error
import com.mclowicz.testfriends.presentation.friends.FriendsState

class FriendsRepository(
    private val userCatalog: UserCatalog
) {

    suspend fun loadFriendsFor(userId: String): FriendsState {
        return try {
            val friendsForUser = userCatalog.loadFriendsFor(userId)
            FriendsState(friends = friendsForUser)
        } catch (e: BackendException) {
            FriendsState(error = Error.Backend)
        } catch (e: OfflineException) {
            FriendsState(error = Error.Offline)
        }
    }

    suspend fun toggleFollowing(userId: String, friendId: String): List<Friend> {
        return userCatalog.toggleFollowing(userId, friendId)
    }
}