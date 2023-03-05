package com.mclowicz.testfriends.presentation.timeline

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.domain.post.Post
import com.mclowicz.testfriends.ui.composables.InfoMessage
import com.mclowicz.testfriends.ui.composables.Loading
import com.mclowicz.testfriends.ui.composables.ScreenTitle
import com.mclowicz.testfriends.ui.extensions.toDateTime

@Composable
fun TimelineScreen(
    userId: String,
    viewModel: TimelineViewModel = hiltViewModel(),
    onCreateNewPost: () -> Unit
) {

    val viewState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.timelineFor(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenTitle(R.string.timeline_screen_title)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxSize()) {
            PostsList(
                posts = viewState.posts,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
            FloatingActionButton(
                onClick = { onCreateNewPost() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .testTag(
                        stringResource(id = R.string.createNewPost)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.createNewPost)
                )
            }
        }
    }
    InfoMessage(message = viewState.error?.msgResId)
    Loading(isVisible = viewState.isLoading)
}

@Composable
fun PostsList(
    posts: List<Post>,
    modifier: Modifier = Modifier,
) {
    if (posts.isEmpty()) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.emptyTimelineMessage)
        )
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(posts) { post ->
                PostItem(post)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = post.userId,
                )
                Text(
                    text = post.timeStamp.toDateTime(),
                )
            }
            Text(
                text = post.postText,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PostsPreview() {
    val posts = (0..100).map { index ->
        Post("$index", "user$index", "This is post number: $index", 1L)
    }
    PostsList(posts = posts)
}