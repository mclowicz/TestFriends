package com.mclowicz.testfriends.presentation.postComposer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.testfriends.domain.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CreatePostState>(CreatePostState())
    val state: StateFlow<CreatePostState> = _state

    private val _event = MutableStateFlow<CreatePostEvent?>(null)
    val event: StateFlow<CreatePostEvent?> = _event

    fun createPost(postText: String) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(isLoading = true))
            val state = withContext(Dispatchers.IO) {
                postRepository.createNewPost(postText)
            }
            if (state.post != null) {
                _event.emit(CreatePostEvent.Created(state.post))
            }
            _state.emit(state)
        }
    }
}