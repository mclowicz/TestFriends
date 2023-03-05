package com.mclowicz.testfriends.presentation.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.testfriends.domain.user.FriendsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val repository: FriendsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FriendsState>(FriendsState())
    val state: StateFlow<FriendsState> = _state

    fun loadFriends(userId: String) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(isLoading = true))
            val result = withContext(Dispatchers.IO) {
                repository.loadFriendsFor(userId).copy(isLoading = false)
            }
            _state.emit(result)
        }
    }

    fun toggleFollowing(userId: String, friendId: String) {
        viewModelScope.launch {
            _state.emit(_state.value.copy(isLoading = true))
            val result = withContext(Dispatchers.IO) {
                repository.toggleFollowing(userId, friendId)
            }
            _state.emit(_state.value.copy(friends = result, isLoading = false))
        }
    }
}