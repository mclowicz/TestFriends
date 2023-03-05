package com.mclowicz.testfriends.presentation.friends

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.domain.user.Friend
import com.mclowicz.testfriends.ui.composables.InfoMessage
import com.mclowicz.testfriends.ui.composables.Loading
import com.mclowicz.testfriends.ui.composables.ScreenTitle

@Composable
fun FriendsScreen(
    userId: String,
    viewModel: FriendsViewModel = hiltViewModel()
) {
    val screenState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadFriends(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenTitle(R.string.friends)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            FriendsList(
                friends = screenState.friends,
                isRefreshing = screenState.isLoading,
                onRefresh = {
                    viewModel.loadFriends(userId)
                },
                onItemClick = { friendId ->
                    viewModel.toggleFollowing(userId = userId, friendId = friendId)
                }
            )
        }
    }
    InfoMessage(message = screenState.error?.msgResId)
    Loading(isVisible = screenState.isLoading)
}

@Composable
fun FriendsList(
    modifier: Modifier = Modifier,
    friends: List<Friend>?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val loadingDescription = stringResource(id = R.string.loading)
    SwipeRefresh(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = loadingDescription },
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh() }
    ) {
        if (friends.isNullOrEmpty()) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.emptyFriendsMessage)
                )
            }
        } else {
            val scrollState = rememberLazyListState()
            LazyColumn(
                modifier = modifier,
                state = scrollState
            ) {
                items(friends) { friend ->
                    FriendItem(friend = friend, onItemClick = { onItemClick(it) })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun FriendItem(
    friend: Friend,
    onItemClick: (String) -> Unit
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .fillMaxWidth()
        .border(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .weight(1f)
            ) {
                Text(text = friend.user.id)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = friend.user.email)
            }
            val contentDescription = stringResource(
                id = if (friend.isFollowee) {
                    R.string.unfollowFriend
                } else {
                    R.string.followFriend
                },
                friend.user.id
            )
            OutlinedButton(
                modifier = Modifier.semantics {
                    this.contentDescription = contentDescription
                },
                onClick = { onItemClick(friend.user.id) }) {
                Text(
                    text = stringResource(
                        id = if (friend.isFollowee) {
                            R.string.unfollow
                        } else {
                            R.string.follow
                        }
                    )
                )
            }
        }
    }
}