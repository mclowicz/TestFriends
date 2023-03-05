package com.mclowicz.testfriends.presentation.postComposer

import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.ui.composables.InfoMessage
import com.mclowicz.testfriends.ui.composables.Loading
import com.mclowicz.testfriends.ui.composables.ScreenTitle

@Composable
fun CreateNewPostScreen(
    viewModel: CreatePostViewModel = hiltViewModel(),
    onPostCreated: () -> Unit
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val viewEvent by viewModel.event.collectAsStateWithLifecycle()

    var postText by remember { mutableStateOf(String()) }

    LaunchedEffect(viewEvent) {
        if (viewEvent is CreatePostEvent.Created) {
            onPostCreated()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        ScreenTitle(resourceId = R.string.createNewPost)
        Box(modifier = Modifier.fillMaxSize()) {
            PostComposer(
                postText = postText,
                onValueChange = { value -> postText = value}
            )
            FloatingActionButton(
                onClick = { viewModel.createPost(postText) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .testTag(stringResource(id = R.string.submitPost))
            ) {
                Icon(
                    imageVector = Icons.Default.Done, 
                    contentDescription = stringResource(id = R.string.submitPost)
                )
            }
        }
    }
    InfoMessage(message = viewState.error?.msgResId)
    Loading(isVisible = viewState.isLoading)
}

@Composable
private fun PostComposer(
    postText: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = postText, 
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.newPostHint)) }
    )
}
